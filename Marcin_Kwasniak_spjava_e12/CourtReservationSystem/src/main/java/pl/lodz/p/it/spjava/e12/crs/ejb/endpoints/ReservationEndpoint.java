package pl.lodz.p.it.spjava.e12.crs.ejb.endpoints;

import pl.lodz.p.it.spjava.e12.crs.ejb.managers.ReservationManager;
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
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ReservationDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.model.Reservation;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)        // interceptor umieszczony przed klasą będzie logował wszystkie metody tej klasy
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ReservationEndpoint {

    @Resource(name = "txRetryLimit")        //parametr txRe.. umieszczony w pliku webxml ustala ilość powtórzeń transakcji
    private int txRetryLimit;

    @EJB
    private ReservationManager reservationManager;        //aby mieć dostęp do listy rezerwacji w DB, muszę mieć wstrzykniętego managera który ma dostęp do encji

    // metoda pobiera listę rezerwacji z bazy danych, oczywiście poprzez wstrzykniętego managera
    public List<ReservationDTO> listAllReservation() {
        List<Reservation> listReservation = reservationManager.findAll();        //pobieram listę wszystkich rezerwacji z fasady poprzez managera
        List<ReservationDTO> listReservationDTO = new ArrayList<>();        //aby zrobić konwersję potrzebuję również listę na obiekty DTO

        // konwersja obiektów encji na DTO za pomocą konstruktora
        for (Reservation reservation : listReservation) {
            //w ramach każdej iteracji obiekt encji przekształcam w DTO i dodaje do listy
            listReservationDTO.add(new ReservationDTO(reservation.getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.isReservationConfirm(), reservation.getDuration()));
        }
        return listReservationDTO;
    }

    // metoda do ponawiania transakcji przy kasowaniu rezerwacji z bazy
    public void removeReservation(ReservationDTO reservationDTO) throws AppBaseException {
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                reservationManager.removeReservation(reservationDTO);
                rollbackTX = reservationManager.isLastTransactionRollback();
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

    // metoda do ponawiania odwołanych transakcji przy zapisie do bazy nowej rezerwacji
    public void createReservation(ReservationDTO reservationDTO, CourtUnitDTO courtUnitDTO) throws AppBaseException {
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                reservationManager.createReservation(reservationDTO, courtUnitDTO);
                rollbackTX = reservationManager.isLastTransactionRollback();
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

    public void confirmReservation(ReservationDTO reservationDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                reservationManager.confirmReservation(reservationDTO);
                rollbackTX = reservationManager.isLastTransactionRollback();
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

    public void unconfirmReservation(ReservationDTO reservationDTO) throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                reservationManager.unconfirmReservation(reservationDTO);
                rollbackTX = reservationManager.isLastTransactionRollback();
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
