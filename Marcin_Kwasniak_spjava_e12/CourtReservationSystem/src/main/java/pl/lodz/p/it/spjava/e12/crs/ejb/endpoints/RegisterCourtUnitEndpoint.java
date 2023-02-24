package pl.lodz.p.it.spjava.e12.crs.ejb.endpoints;

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
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.ejb.managers.CourtUnitManager;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RegisterCourtUnitEndpoint {

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    @EJB
    private CourtUnitManager courtUnitManager;

    public void saveRegisterCourtUnit(CourtUnitDTO courtUnitDTO) throws AppBaseException {     //metoda nic nie zwraca (void) gdyż ona przyjmuje dane i wysyła do DB
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                courtUnitManager.saveRegisterCourtUnit(courtUnitDTO);
                rollbackTX = courtUnitManager.isLastTransactionRollback();
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
