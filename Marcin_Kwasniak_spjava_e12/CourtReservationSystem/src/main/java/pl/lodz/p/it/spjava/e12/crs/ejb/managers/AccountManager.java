package pl.lodz.p.it.spjava.e12.crs.ejb.managers;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.AccountDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.KontoException;
import pl.lodz.p.it.spjava.e12.crs.model.Account;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager extends AbstractManager implements SessionSynchronization {

    @EJB
    private AccountFacade accountFacade;

    // dane klienta wczytane w pole stanu przed rozpoczęciem edycji (dla blokad optymistycznych)
    private Account accountState;

    public void createAccount(ClientDTO clientDTO) {

        //Logika związana z zapisem edytowanego konta do bazy danych
        Account account = new Account();        //encja clienta, która będzie zapisywana w bazie
        account.setKind(clientDTO.getKind());
        account.setPassword(clientDTO.getPassword());
        account.setLogin(clientDTO.getLogin());
        account.setActive(clientDTO.isActive());
        account.setConfirm(clientDTO.isConfirm());
        accountFacade.create(account);      //utrwalenie (zapis) w bazie danych
    }

    // metoda do usuwania z bazy (poprzez fasadę) encji konta
    public void removeAccount(ClientDTO clientDTO) throws AppBaseException {
        Account account = accountFacade.find(clientDTO.getId());
        if (null == account) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        accountFacade.remove(account);
    }

    // metoda do wczytania obiektu klienta z DB i umieszczenia w polu "accountState"
    public void loadAccountInState(ClientDTO clientDTO) {
        accountState = accountFacade.find(clientDTO.getId());       // pozyskuję obiekt konta z DB poprzez fasadę
    }

    // metoda która prowadzi do wczytania zapamiętanego obiektu (z pola "accountState) i wysłania do metody w endpoincie
    public ClientDTO loadAccountFromState() throws AppBaseException {
        if (accountState == null) {
            throw new IllegalStateException("ERROR.Brak.encji.w stanie.komponentu.EJB");
        } else {      //zwracam obiekt DTO który ma cechy zaczytane ze stanu
            return new ClientDTO(accountState.getId(), accountState.getKind(), accountState.getPassword(), accountState.getLogin());
        }
    }

    // metoda zapisująca zmienione (po edycji) dane klienta
    public void editAccount(ClientDTO clientDTO) throws KontoException, AppBaseException {

        Account account = accountFacade.find(clientDTO.getId());
        if (null == account) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        // reguły logiki związane z zapisem edytowanych danych klienta
        account.setKind(clientDTO.getKind());
        account.setPassword(clientDTO.getPassword());
        account.setActive(clientDTO.isActive());
        account.setConfirm(clientDTO.isConfirm());
        account.setLogin(clientDTO.getLogin());
        accountFacade.edit(account);
    }

    public List<Account> findAll() {
        return accountFacade.findAll();
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void activateAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = accountFacade.find(accountDTO.getId());
        if (null == account) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        account.setActive(true);
    }

    @RolesAllowed("Pracownik")
    public void deactivateAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = accountFacade.find(accountDTO.getId());
        if (null == account) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        account.setActive(false);
    }
}
