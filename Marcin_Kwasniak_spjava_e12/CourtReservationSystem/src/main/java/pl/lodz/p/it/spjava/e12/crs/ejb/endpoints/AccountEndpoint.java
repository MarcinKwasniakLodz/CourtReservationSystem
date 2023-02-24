package pl.lodz.p.it.spjava.e12.crs.ejb.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.AccountDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.ejb.managers.AccountManager;
import pl.lodz.p.it.spjava.e12.crs.ejb.managers.ClientManager;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.KontoException;
import pl.lodz.p.it.spjava.e12.crs.model.Account;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.NEVER)       // NEVER bo endpoint nie uczestniczy w transakcji
public class AccountEndpoint {

    @EJB
    private AccountManager accountManager;

    @EJB
    private ClientManager clientManager;

    @Inject
    private AccountFacade accountFacade;

    public List<ClientDTO> listAllAccount() {
        List<Account> listAccount = accountManager.findAll();       //lista z encjami JPA typu konto
        List<ClientDTO> listClientDTO = new ArrayList<>();      //lista z obiektami DTO typu AccountDTO

        //teraz robie konwersję aby zapłnić listę AccountDTO obiektami encyjnymi Account
        for (Account account : listAccount) {
            ClientDTO clientDTO = new ClientDTO(account.getId());
            clientDTO.setKind(account.getKind());
            clientDTO.setPassword(account.getPassword());     //można to zrobić również konstruktorem zamiast przepisywać
            clientDTO.setLogin(account.getLogin());
            clientDTO.setActive(account.isActive());
            clientDTO.setConfirm(account.isConfirm());
            listClientDTO.add(clientDTO);
        }
        return listClientDTO;
    }

    @Resource(name = "txRetryLimit")        //parametr txRe.. umieszczony w pliku webxml ustala ilość powtórzeń transakcji
    private int txRetryLimit;

    // metoda do ponawiania odwołanych transakcji przy zapisie do bazy nowego konta
    @PermitAll
    public void createAccount(ClientDTO clientDTO) throws AppBaseException {
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.createClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName() + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw KontoException.createKontoExceptionWithTxRetryRollback();
        }
    }

    public void activateAccount(AccountDTO accountDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.activateAccount(accountDTO);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName() + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw new IllegalStateException("${msg['error.konto.db.constraint.transaction.limit']}");
        }
    }

    public void deactivateAccount(AccountDTO accountDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.deactivateAccount(accountDTO);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter + " wykonania metody biznesowej zakończona wyjątkiem klasy: "
                        + ex.getClass().getName() + " z komunikatem: " + ex.getMessage());
                rollbackTX = true;
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw new IllegalStateException("${msg['error.konto.db.constraint.transaction.limit']}");
        }
    }
}
