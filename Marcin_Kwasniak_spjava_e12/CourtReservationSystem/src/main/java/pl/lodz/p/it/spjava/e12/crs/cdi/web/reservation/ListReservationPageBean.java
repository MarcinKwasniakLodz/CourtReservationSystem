package pl.lodz.p.it.spjava.e12.crs.cdi.web.reservation;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import pl.lodz.p.it.spjava.e12.crs.dto.ReservationDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ReservationEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.ReservationException;

/**
 *
 * @author marci
 */
@Named(value = "listReservationPageBean")
@RequestScoped
public class ListReservationPageBean {      //w tej klasie pobieram listę rezerwacji z DTO i udostępniam ją dla widoku

    @EJB
    private ReservationEndpoint reservationEndpoint;

    private List<ReservationDTO> listReservationDTO;

    public List<ReservationDTO> getListReservationDTO() {
        return listReservationDTO;
    }

    public void setListReservationDTO(List<ReservationDTO> listReservationDTO) {
        this.listReservationDTO = listReservationDTO;
    }

    public ListReservationPageBean() {
    }

    @PostConstruct
    private void init() {
        listReservationDTO = reservationEndpoint.listAllReservation();
    }

    public String deleteReservationAction(ReservationDTO reservationDTO) throws ReservationException {
        try {
            reservationEndpoint.removeReservation(reservationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListReservationPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (null == reservationDTO) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        } else {
            return "actionReservationSuccess";
        }
    }

    public String confirmReservationAction(ReservationDTO reservationDTO) throws ReservationException {
        try {
            reservationEndpoint.confirmReservation(reservationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListReservationPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (null == reservationDTO) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        } else {
            return "actionReservationSuccess";
        }
    }

    public String unconfirmReservationAction(ReservationDTO reservationDTO) throws ReservationException {
        try {
            reservationEndpoint.unconfirmReservation(reservationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListReservationPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (null == reservationDTO) {
            throw ReservationException.createReservationExceptionWithNoStateInEJB();
        } else {
            return "actionReservationSuccess";
        }
    }

}
