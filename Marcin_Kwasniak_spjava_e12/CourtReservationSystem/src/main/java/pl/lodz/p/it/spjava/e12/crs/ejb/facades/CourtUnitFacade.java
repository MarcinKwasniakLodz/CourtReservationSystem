package pl.lodz.p.it.spjava.e12.crs.ejb.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.model.CourtUnit;

/**
 *
 * @author marci
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourtUnitFacade extends AbstractFacade<CourtUnit> {

    @PersistenceContext(unitName = "CourtReservationSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourtUnitFacade() {
        super(CourtUnit.class);
    }

    //tutaj otrzymuję obiekt courtUnit (typizowanie zapytanie)
    public CourtUnit findByCourtName(final String courtName) {
        TypedQuery<CourtUnit> tq = em.createNamedQuery("CourtUnit.findByCourtName", CourtUnit.class);
        tq.setParameter("courtName", courtName);
        CourtUnit courtUnit = tq.getSingleResult();
        em.refresh(courtUnit);
        return courtUnit;
    }
}
