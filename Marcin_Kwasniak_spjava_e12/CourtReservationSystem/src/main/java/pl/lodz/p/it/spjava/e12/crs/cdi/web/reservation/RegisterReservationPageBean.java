package pl.lodz.p.it.spjava.e12.crs.cdi.web.reservation;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ReservationDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.CourtUnitEndpoint;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ReservationEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.utils.ContextUtils;

/**
 *
 * @author marci
 */
@Named(value = "registerReservationPageBean")
@RequestScoped
public class RegisterReservationPageBean {

    @EJB
    private ReservationEndpoint reservationEndpoint;

    @EJB
    private CourtUnitEndpoint courtUnitEndpoint;   //wstrzyknięcie listy kortów

    private List<CourtUnitDTO> listCourtUnitDTO;   //z pola listCourtUnitDTO get'erem pobieram dane i wrzucam je na stronę web

    private CourtUnitDTO choosenCourtUnitDTO;   // pole do przechowywania wybranego kortu w formularza web (+ geter i seter)

    private ReservationDTO savedReservationDTO;   //pole zapisywanej rezerwacji

    public ReservationDTO getSavedReservationDTO() {
        return savedReservationDTO;
    }

    public void setSavedReservationDTO(ReservationDTO savedReservationDTO) {
        this.savedReservationDTO = savedReservationDTO;
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

    //komponent CDI (Context and Dependency Injection) do wstrzykiwania danych z DB do formularza web
    public RegisterReservationPageBean() {
    }

    //metoda zaczytuje listę kortów i umieszcza ją w polu listCourtUnitDTO
    @PostConstruct  // metoda będzie wykonana po wstrzyknięciu (po zbudowaniu strony web)
    public void init() {
        listCourtUnitDTO = courtUnitEndpoint.listAllCourtUnit();
        choosenCourtUnitDTO = new CourtUnitDTO();
        savedReservationDTO = new ReservationDTO();
    }

    // bezparametrowa metoda zwracająca string do sprawdzenia poprawności wyboru kortu
    public String createReservationAction() throws AppBaseException {
        try {

            if (!(savedReservationDTO.getStartDate().before(savedReservationDTO.getEndDate()))) {
                ContextUtils.emitInternationalizedMessage("Niepoprawne dane", "error.reservation.wrong.date");
                return "";
            } else {
                reservationEndpoint.createReservation(savedReservationDTO, choosenCourtUnitDTO);
            }

        } catch (AppBaseException ex) {
            ContextUtils.emitInternationalizedMessage("Niepoprawne dane", ex.getMessage());
            Logger.getLogger(ListReservationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return "createReservationSuccess"; //gdy metoda zwraca null to pozostaje na tej samej stronie, ta ma iść na listę rezerwacji
    }
}
