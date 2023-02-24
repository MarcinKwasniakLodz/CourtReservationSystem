package pl.lodz.p.it.spjava.e12.crs.ejb.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.ejb.managers.ClientManager;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.model.Client;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)        // interceptor umieszczony przed klasą będzie logował wszystkie metody tej klasy
@TransactionAttribute(TransactionAttributeType.NEVER)       // never, bo endpoint nie uczestniczy w transakcji i metody realizujące ponowienie transakcji nie powinny w niej uczestniczyć
public class ClientEndpoint {

    @EJB
    private ClientManager clientManager;

    public List<ClientDTO> listAllClient() {
        List<Client> listClient = clientManager.findAll();       //lista z encjami JPA typu klient
        List<ClientDTO> listClientDTO = new ArrayList<>();      //lista z obiektami DTO typu ClientDTO

        //teraz robie konwersję aby zapłnić listę ClientDTO obiektami encyjnymi Client
        for (Client client : listClient) {
            ClientDTO clientDTO = new ClientDTO(client.getId());
            clientDTO.setFirstName(client.getFirstName());     //można to zrobić również konstruktorem zamiast przepisywać
            clientDTO.setLastName(client.getLastName());
            clientDTO.setEmail(client.getEmail());
            clientDTO.setPhoneNumber(client.getPhoneNumber());
            clientDTO.setConfirm(client.getAccount().isConfirm());
            clientDTO.setActive(client.getAccount().isActive());
            listClientDTO.add(clientDTO);
        }
        return listClientDTO;
    }

    @Resource(name = "txRetryLimit")        //parametr txRe.. umieszczony w pliku webxml ustala ilość powtórzeń transakcji
    private int txRetryLimit;

    // metoda do edycji danych klienta - przetrzymuje dane w "stanie" podczas edycji
    public void loadClientInState(ClientDTO clientDTO) throws AppBaseException {
        boolean rollbackTX;     //początek mechanizmu ponawiania transakcji

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.loadClientInState(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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

    // metoda pobiera listę klientów z bazy danych, oczywiście poprzez managera
    public List<ClientDTO> listAllCreateClient() {
        List<Client> listClient = clientManager.findAll();        //pobieram listę wszystkich klientów z fasady poprzez managera
        List<ClientDTO> listClientDTO = new ArrayList<>();        //aby zrobić konwersję potrzebuję również listę na obiekty DTO

        // konwersja obiektów encji na DTO za pomocą konstruktora
        for (Client client : listClient) {
            //w ramach każdej iteracji obiekt encji przekształcam w DTO i dodaje do listy
            listClientDTO.add(new ClientDTO(client.getId(), client.getFirstName(), client.getLastName(), client.getEmail(), client.getPhoneNumber()));
        }
        return listClientDTO;
    }

    // metoda do ponawiania odwołanych transakcji przy zapisie do bazy nowego klienta
    public void createClient(ClientDTO clientDTO) throws AppBaseException {
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
            throw new IllegalStateException("${msg['error.konto.db.constraint.transaction.limit']}");
        }
    }

    // metoda do ponawiania transakcji przy kasowaniu klienta z bazy
    public void removeClient(ClientDTO clientDTO) throws AppBaseException {
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.removeClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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

    public void editClient(ClientDTO clientDTO) throws AppBaseException {
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.editClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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

    public void activateClient(ClientDTO clientDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.activateClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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

    public void deactivateClient(ClientDTO clientDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.deactivateClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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

    public void confirmClient(ClientDTO clientDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientManager.confirmClient(clientDTO);
                rollbackTX = clientManager.isLastTransactionRollback();
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
