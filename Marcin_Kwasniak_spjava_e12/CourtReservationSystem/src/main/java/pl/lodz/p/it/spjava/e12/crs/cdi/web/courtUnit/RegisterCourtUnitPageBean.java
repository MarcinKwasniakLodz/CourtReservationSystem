package pl.lodz.p.it.spjava.e12.crs.cdi.web.courtUnit;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.CourtUnitEndpoint;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.RegisterCourtUnitEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;

/**
 *
 * @author marci
 */
@Named(value = "registerCourtUnitPageBean")
@RequestScoped
public class RegisterCourtUnitPageBean {

    @EJB
    private RegisterCourtUnitEndpoint registerCourtUnitEndpoint;

    @EJB
    private CourtUnitEndpoint courtUnitEndpoint;

    private List<CourtUnitDTO> listCourtUnitDTO;

    private CourtUnitDTO choosenCourtUnitDTO;

    private CourtUnitDTO savedCourtUnitDTO;

    public CourtUnitDTO getSavedCourtUnitDTO() {
        return savedCourtUnitDTO;
    }

    public void setSavedCourtUnitDTO(CourtUnitDTO savedCourtUnitDTO) {
        this.savedCourtUnitDTO = savedCourtUnitDTO;
    }

    public CourtUnitDTO getChoosenCourtUnitDTO() {
        return choosenCourtUnitDTO;
    }

    public void setChoosenCourtUnitDTO(CourtUnitDTO choosenCourtUnitDTO) {
        this.choosenCourtUnitDTO = choosenCourtUnitDTO;
    }

    public List<CourtUnitDTO> getListCourtUnitDTO() {
        return listCourtUnitDTO;
    }

    public void setListCourtUnitDTO(List<CourtUnitDTO> listCourtUnitDTO) {
        this.listCourtUnitDTO = listCourtUnitDTO;
    }

    public RegisterCourtUnitPageBean() {
    }

    @PostConstruct
    public void init() {
        listCourtUnitDTO = courtUnitEndpoint.listAllCourtUnit();    //do listCourtUnitDTO wpisuję wartość z metody listAllCourtUnit() z courtUnitEndpoint
        //choosenCourtUnitDTO = new CourtUnitDTO();  //opcja dla listy rozwijanej
        savedCourtUnitDTO = new CourtUnitDTO();
    }

    public String saveRegisterCourtUnit() throws AppBaseException {
        registerCourtUnitEndpoint.saveRegisterCourtUnit(savedCourtUnitDTO);
        return "registerCourtUnitSuccess";
    }

}
