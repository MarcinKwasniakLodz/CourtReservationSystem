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
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.CourtUnitFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.ejb.managers.CourtUnitManager;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.model.CourtUnit;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CourtUnitEndpoint {

    @Resource(name = "txRetryLimit")        //parametr txRe.. umieszczony w pliku webxml ustala ilość powtórzeń transakcji
    private int txRetryLimit;

    @Inject
    private CourtUnitFacade courtUnitFacade;

    @EJB
    private CourtUnitManager courtUnitManager;     //wstrzyknięty manager, więc kontener dostarcza zawartość pola courtUnitFacade poprzez metodę managera

    public List<CourtUnitDTO> listAllCourtUnit() {  //kolekcja zawierająca encje typu CourtUnit aby dostarczyć je do widoku
        List<CourtUnit> listCourtUnit = courtUnitManager.findAll();  //lista z encjami JPA typu CourtUnit
        List<CourtUnitDTO> listCourtUnitDTO = new ArrayList<>();   //lista z obiektami DTO typu CourtUnitDTO

        for (CourtUnit courtUnit : listCourtUnit) { //konwersja z CourtUnit na CourtUnitDTO, czyli przepisanie cech z jednego obiektu na drugi
            CourtUnitDTO courtUnitDTO = new CourtUnitDTO(courtUnit.getId());
            courtUnitDTO.setCourtName(courtUnit.getCourtName());     //można to zrobić również konstruktorem zamiast przepisywać
            courtUnitDTO.setCourtNumber(courtUnit.getCourtNumber());     // obiektDTO.setCośCoChcęUstawić(skądPobieramObiekt.getMetodaDostępowa()
            courtUnitDTO.setTypeOfCourt(courtUnit.getTypeOfCourt());    //każdy obiekt DTO powinien być tego samego typu co w encji, jeżeli chodzi 
            courtUnitDTO.setTypeOfSurface(courtUnit.getTypeOfSurface());    // o cechy biznesowe
            courtUnitDTO.setAvailable(courtUnit.isAvailable());
            listCourtUnitDTO.add(courtUnitDTO);
        }
        return listCourtUnitDTO;
    }

    public void removeCourtUnit(CourtUnitDTO courtUnitDTO)  throws AppBaseException {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                courtUnitManager.removeCourtUnit(courtUnitDTO);
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

    public void activateCourtUnit(CourtUnitDTO courtUnitDTO) throws AppBaseException  {

        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                courtUnitManager.activateCourtUnit(courtUnitDTO);
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

    public void deactivateCourtUnit(CourtUnitDTO courtUnitDTO)  throws AppBaseException {
        
        boolean rollbackTX;

        int retryTXCounter = txRetryLimit;

        do {
            try {
                courtUnitManager.deactivateCourtUnit(courtUnitDTO);
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
