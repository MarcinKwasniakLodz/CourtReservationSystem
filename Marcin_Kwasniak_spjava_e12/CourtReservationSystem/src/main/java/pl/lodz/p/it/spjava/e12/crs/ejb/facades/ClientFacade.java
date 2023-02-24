package pl.lodz.p.it.spjava.e12.crs.ejb.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.model.Client;

/**
 *
 * @author marci
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "CourtReservationSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }
}
