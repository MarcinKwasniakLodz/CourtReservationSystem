package pl.lodz.p.it.spjava.e12.crs.cdi.web.courtUnit;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.CourtUnitEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;

/**
 *
 * @author marci
 */
@Named(value = "listCourtUnitPageBean")
@RequestScoped
public class ListCourtUnitPageBean {

    @EJB
    private CourtUnitEndpoint courtUnitEndpoint;

    @Inject
    private CourtUnitDTO courtUnitDTO;

    private List<CourtUnitDTO> listCourtUnitDTO;

    public List<CourtUnitDTO> getListCourtUnitDTO() {
        return listCourtUnitDTO;
    }

    public void setListCourtUnitDTO(List<CourtUnitDTO> listCourtUnitDTO) {
        this.listCourtUnitDTO = listCourtUnitDTO;
    }

    public ListCourtUnitPageBean() {

    }

    @PostConstruct
    private void init() {
        listCourtUnitDTO = courtUnitEndpoint.listAllCourtUnit();
    }

    public String deleteCourtUnitAction(CourtUnitDTO courtUnitDTO) throws AppBaseException {
        courtUnitEndpoint.removeCourtUnit(courtUnitDTO);
        return "actionCourtUnitSuccess";
    }

    public String activateCourtUnitAction(CourtUnitDTO courtUnitDTO) throws AppBaseException {
        courtUnitEndpoint.activateCourtUnit(courtUnitDTO);
        return "actionCourtUnitSuccess";
    }

    public String deactivateCourtUnitAction(CourtUnitDTO courtUnitDTO) throws AppBaseException {
        courtUnitEndpoint.deactivateCourtUnit(courtUnitDTO);
        return "actionCourtUnitSuccess";
    }

}
