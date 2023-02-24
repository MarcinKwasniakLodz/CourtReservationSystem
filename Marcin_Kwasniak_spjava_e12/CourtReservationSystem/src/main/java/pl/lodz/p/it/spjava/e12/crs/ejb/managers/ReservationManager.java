package pl.lodz.p.it.spjava.e12.crs.ejb.managers;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.e12.crs.dto.CourtUnitDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ReservationDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.CourtUnitFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.ReservationFacade;
import pl.lodz.p.it.spjava.e12.crs.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.exception.ReservationException;
import pl.lodz.p.it.spjava.e12.crs.model.CourtUnit;
import pl.lodz.p.it.spjava.e12.crs.model.Reservation;

/**
 *
 * @author marci
 */
@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReservationManager extends AbstractManager implements SessionSynchronization {

    @EJB
    private CourtUnitFacade courtUnitFacade;

    @EJB
    private ReservationFacade reservationFacade;
    private Throwable cause;

    // metoda do usuwania z bazy (poprzez fasadę) encji rezerwacji
    public void removeReservation(ReservationDTO reservationDTO) throws ReservationException, AppBaseException {
        Reservation reservation = reservationFacade.find(reservationDTO.getId());
        if (null == reservation) {
            throw ReservationException.createReservationExceptionWithDbCheckConstraintKey(reservation, cause);
        } else {
            reservationFacade.remove(reservation);
        }
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void confirmReservation(ReservationDTO reservationDTO) throws ReservationException, AppBaseException {
        Reservation reservation = reservationFacade.find(reservationDTO.getId());
        if (null == reservation) {
            throw ReservationException.createReservationExceptionWithDbCheckConstraintKey(reservation, cause);
        } else {
            reservation.setReservationConfirm(true);
        }
    }

    @RolesAllowed({"Pracownik", "Administrator"})
    public void unconfirmReservation(ReservationDTO reservationDTO) throws ReservationException, AppBaseException {
        Reservation reservation = reservationFacade.find(reservationDTO.getId());
        if (null == reservation) {
            throw ReservationException.createReservationExceptionWithDbCheckConstraintKey(reservation, cause);
        } else {
            reservation.setReservationConfirm(false);
        }
    }

    public void createReservation(ReservationDTO reservationDTO, CourtUnitDTO courtUnitDTO) throws ReservationException {

        CourtUnit courtUnit = courtUnitFacade.findByCourtName(courtUnitDTO.getCourtName());
        if (!courtUnit.isAvailable()) {
            throw ReservationException.createReservationExceptionWithCourtNotAvailable();
        }
        //Logika związana z zapisem rezerwacji do bazy danych
        Reservation reservation = new Reservation();        //encja rezerwacji, która będzie zapisywana w bazie
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setDuration(reservationDTO.getDuration());

        //Powiązanie dwukierunlowe encji rezerwacji i kortów
        reservation.setCourtUnit(courtUnit);
        courtUnit.getListReservation().add(reservation);
        reservationFacade.create(reservation);      //utrwalenie (zapis) w bazie danych
    }

    public List<Reservation> findAll() {
        return reservationFacade.findAll();
    }
}
