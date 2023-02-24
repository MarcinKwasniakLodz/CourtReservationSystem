package pl.lodz.p.it.spjava.e12.crs.ejb.managers;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.ClientFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.KontoException;
import pl.lodz.p.it.spjava.e12.crs.exception.ReservationException;
import pl.lodz.p.it.spjava.e12.crs.model.Account;
import pl.lodz.p.it.spjava.e12.crs.model.Client;
import static pl.lodz.p.it.spjava.e12.crs.model.Client_.account;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ClientManager extends AbstractManager implements SessionSynchronization {

    @EJB
    private ClientFacade clientFacade;

    // dane klienta wczytane w pole stanu przed rozpoczęciem edycji (dla blokad optymistycznych)
    private Client clientState;

    public void createClient(ClientDTO clientDTO) {

        //Logika związana z zapisem edytowanego klienta do bazy danych
        Client client = new Client();        //encja clienta, która będzie zapisywana w bazie
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        Account account = new Account();
        account.setPassword(clientDTO.getPassword());
        account.setLogin(clientDTO.getLogin());
        account.setActive(false);
        account.setConfirm(false);
        account.setKind(clientDTO.getKind());
        client.setAccount(account);
        account.setClient(client);
        clientFacade.create(client);      //utrwalenie (zapis) w bazie danych
    }

    // metoda do usuwania z bazy (poprzez fasadę) encji klienta
    public void removeClient(ClientDTO clientDTO) throws ReservationException, AppBaseException {
        Client client = clientFacade.find(clientDTO.getId());
        if (null == account) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        }
        clientFacade.remove(client);
    }

    // metoda do wczytania obiektu klienta z DB i umieszczenia w polu "clientState"
    public void loadClientInState(ClientDTO clientDTO) throws ReservationException {
        clientState = clientFacade.find(clientDTO.getId());       // pozyskuję obiekt client z DB poprzez fasadę
        if (null == account) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        }
    }

    // metoda która prowadzi do wczytania zapamiętanego obiektu (z pola "clientState) i wysłania do metody w endpoincie
    public ClientDTO loadClientFromState() {
        if (clientState == null) {
            throw new IllegalStateException("ERROR.Brak.encji.w stanie.komponentu.EJB");
        } else {      //zwracam obiekt DTO który ma cechy zaczytane ze stanu
            return new ClientDTO(clientState.getId(), clientState.getFirstName(), clientState.getLastName(), clientState.getEmail(), clientState.getPhoneNumber());
        }
    }

    // metoda zapisująca zmienione (po edycji) dane klienta
    public void editClient(ClientDTO clientDTO) throws ReservationException, AppBaseException {
        Client client = clientFacade.find(clientDTO.getId());
        if (null == account) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        }
        // reguły logiki związane z zapisem edytowanych danych klienta
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());

        clientFacade.edit(client);
    }

    public List<Client> findAll() {
        return clientFacade.findAll();
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void activateClient(ClientDTO clientDTO) throws KontoException {
        Client client = clientFacade.find(clientDTO.getId());
        if (null == client) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        client.getAccount().setActive(true);
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void deactivateClient(ClientDTO clientDTO) throws KontoException {
        Client client = clientFacade.find(clientDTO.getId());
        if (null == client) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        client.getAccount().setActive(false);
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void confirmClient(ClientDTO clientDTO) throws KontoException {
        Client client = clientFacade.find(clientDTO.getId());
        if (null == account) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        client.getAccount().setConfirm(true);
    }
}
