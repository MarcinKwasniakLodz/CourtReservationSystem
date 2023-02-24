package pl.lodz.p.it.spjava.e12.crs.ejb.facades;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.KontoException;
import pl.lodz.p.it.spjava.e12.crs.model.Account;
import pl.lodz.p.it.spjava.e12.crs.model.Account_;
import pl.lodz.p.it.spjava.e12.crs.model.Reservation;

/**
 *
 * @author marci
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "CourtReservationSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    public void lock(Reservation reservation) {
        em.lock(reservation, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    @Override
    public void edit(Account entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (javax.persistence.OptimisticLockException oe) {
            throw KontoException.createKontoExceptionWithOptimisticLockKey(entity, oe);
        }
    }

    @Override
    public void remove(Account entity) throws AppBaseException {
        try {
            super.remove(entity);
            em.flush();
        } catch (javax.persistence.OptimisticLockException oe) {
            throw KontoException.createKontoExceptionWithOptimisticLockKey(entity, oe);
        }
    }

    private static final Logger LOG = Logger.getLogger(AccountFacade.class.getName());

    // Ta metoda jest używana przez implementację uwierzytelniania zgodnie z Soteria
    @ExcludeClassInterceptors //Nie chcemy ujawniać w dziennikach skrótu hasła
    @RolesAllowed("AUTHENTICATOR") //"Zwykłe" role nie mają tu dostępu. Musi pośredniczyć odpowiedni endpoint opisany jako @RunAs("AUTHENTICATOR").  //todo!!!!!!!!!!!!!!!!!
    public Account znajdzLoginISkrotHaslaWsrodAktywnychIPotwierdzonychKont(String login, String skrotHasla) {
        if (null == login || null == skrotHasla || login.isEmpty() || skrotHasla.isEmpty()) {
            return null;
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> from = query.from(Account.class);
        Predicate criteria = cb.conjunction();
        criteria = cb.and(criteria, cb.equal(from.get(Account_.login), login));
        criteria = cb.and(criteria, cb.equal(from.get(Account_.password), skrotHasla));
        criteria = cb.and(criteria, cb.isTrue(from.get(Account_.active)));
        criteria = cb.and(criteria, cb.isTrue(from.get(Account_.confirm)));
        query = query.select(from);
        query = query.where(criteria);
        TypedQuery<Account> tq = em.createQuery(query);

        try {
            return tq.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Authentication for login: {0} failed with: {1}", new Object[]{login, ex});
        }
        return null;
    }
}
