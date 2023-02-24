package pl.lodz.p.it.spjava.e12.crs.ejb.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.ReservationException;
import pl.lodz.p.it.spjava.e12.crs.model.Reservation;

/**
 *
 * @author marci
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY) //umieszczone przed klsą, więc dotyczy wszystkich metod
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = "CourtReservationSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservationFacade() {
        super(Reservation.class);
    }

    public void lock(Reservation reservation) {
        em.lock(reservation, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    @Override
    public void edit(Reservation entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw ReservationException.createReservationExceptionWithOptimisticLockKey(entity, oe);
        }
    }

    @Override
    public void remove(Reservation entity) throws AppBaseException {
        try {
            super.remove(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw ReservationException.createReservationExceptionWithOptimisticLockKey(entity, oe);
        }
    }
}
