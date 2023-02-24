package pl.lodz.p.it.spjava.e12.crs.exception;

import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.e12.crs.model.Reservation;

/**
 *
 */
public class ReservationException extends AppBaseException {

    static final public String KEY_OPTIMISTIC_LOCK = "error.reservation.optimisticlock";
    static final public String KEY_DB_CONSTRAINT = "error.reservation.db.constraint";
    static final public String KEY_APROVE_OF_APROVED = "error.reservation.aprove.of.aproved";
    static final public String KEY_COURT_NOT_AVAILABLE = "error.reservation.court.not.available";
    static final public String KEY_NO_STATE_IN_EJB = "error.reservation.no.state.in.ejb";

    private ReservationException(String message) {
        super(message);
    }

    private ReservationException(String message, Throwable cause) {
        super(message, cause);
    }
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    static public ReservationException createReservationExceptionWithTxRetryRollback() {
        ReservationException re = new ReservationException(KEY_TX_RETRY_ROLLBACK);
        return re;
    }

    static public ReservationException createReservationExceptionWithOptimisticLockKey(Reservation reservation, OptimisticLockException cause) {
        ReservationException re = new ReservationException(KEY_OPTIMISTIC_LOCK, cause);
        re.setReservation(reservation);
        return re;
    }

    static public ReservationException createReservationExceptionWithDbCheckConstraintKey(Reservation reservation, Throwable cause) {
        ReservationException re = new ReservationException(KEY_DB_CONSTRAINT, cause);
        re.setReservation(reservation);
        return re;
    }

    static public ReservationException createReservationExceptionWithAproveOfAproved(Reservation reservation) {
        ReservationException re = new ReservationException(KEY_APROVE_OF_APROVED);
        re.setReservation(reservation);
        return re;
    }

    static public ReservationException createReservationExceptionWithNoStateInEJB() {
        ReservationException re = new ReservationException(KEY_NO_STATE_IN_EJB);
        return re;
    }

    static public ReservationException createReservationExceptionWithCourtNotAvailable() {
        ReservationException re = new ReservationException(KEY_COURT_NOT_AVAILABLE);
        return re;
    }

}
