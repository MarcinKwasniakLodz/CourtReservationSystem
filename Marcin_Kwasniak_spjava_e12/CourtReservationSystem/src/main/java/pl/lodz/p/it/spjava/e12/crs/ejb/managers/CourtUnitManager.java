package pl.lodz.p.it.spjava.e12.crs.ejb.managers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.CourtUnitFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.KontoException;
import pl.lodz.p.it.spjava.e12.crs.model.CourtUnit;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CourtUnitManager extends AbstractManager implements SessionSynchronization {

    @EJB
    private CourtUnitFacade courtUnitFacade;

    public List<CourtUnit> findAll() {
        return courtUnitFacade.findAll();
    }

    public void saveRegisterCourtUnit(CourtUnitDTO courtUnitDTO) {     //metoda nic nie zwraca (void) gdyż ona przyjmuje dane i wysyła do DB

        System.out.println("Nazwa wybranego kortu " + courtUnitDTO.getCourtName());

        // CourtUnit courtUnit = courtUnitFacade.findByCourtName(courtUnitDTO.getCourtName());
        CourtUnit courtUnit = new CourtUnit();      //encja kortu zapisywana w bazie
        courtUnit.setCourtNumber(courtUnitDTO.getCourtNumber());
        courtUnit.setCourtName(courtUnitDTO.getCourtName());
        courtUnit.setTypeOfCourt(courtUnitDTO.getTypeOfCourt());
        courtUnit.setTypeOfSurface(courtUnitDTO.getTypeOfSurface());
        courtUnitFacade.create(courtUnit);
    }

    public List<CourtUnitDTO> listAllCourtUnit() {      //metoda bezparametrowa, zwracająca listę kortów
        List<CourtUnit> listCourtUnit = courtUnitFacade.findAll();       // lista wszystkich kortów pobrana z fasady i przechowana
        List<CourtUnitDTO> listCourtUnitDTO = new ArrayList<>();        // tworzę listę na obiekty CoutUnitDTO, bo żeby przekonwertować obiekty encji na DTO, muszę mieć przygotowaną listę
        // konwersja obiektów encji na DTO za pomocą konstruktora
        for (CourtUnit courtUnit : listCourtUnit) {       //iteruję po elementach encji
            listCourtUnitDTO.add(new CourtUnitDTO(courtUnit.getId(), courtUnit.getCourtName(), courtUnit.getCourtNumber(),
                    courtUnit.getTypeOfCourt(), courtUnit.getTypeOfSurface()));      // dodaje do listy pola pobrane z encji poprzez konstruktor
        }
        return listCourtUnitDTO;
    }

    @RolesAllowed("Pracownik")
    public void removeCourtUnit(CourtUnitDTO courtUnitDTO) throws KontoException, AppBaseException {
        CourtUnit courtUnit = courtUnitFacade.find(courtUnitDTO.getId());       // w polu courtUnit przechowuję encję pobraną z DB na podstawie Id
        if (null == courtUnit) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        courtUnitFacade.remove(courtUnit);      // z fasady za pomocą operacji remove kasuje zapamiętaną encję w polu courtUnit
    }

    @RolesAllowed("Pracownik")
    public void activateCourtUnit(CourtUnitDTO courtUnitDTO) throws KontoException {
        CourtUnit courtUnit = courtUnitFacade.find(courtUnitDTO.getId());
        if (null == courtUnit) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        courtUnit.setAvailable(true);
    }

    @RolesAllowed("Pracownik")
    public void deactivateCourtUnit(CourtUnitDTO courtUnitDTO) throws KontoException {
        CourtUnit courtUnit = courtUnitFacade.find(courtUnitDTO.getId());
        if (null == courtUnit) {
            throw KontoException.exceptionRequiredDataIsNotAvailable();
        }
        courtUnit.setAvailable(false);
    }
}
