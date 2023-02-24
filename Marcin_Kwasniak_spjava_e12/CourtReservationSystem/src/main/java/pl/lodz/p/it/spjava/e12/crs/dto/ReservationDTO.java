package pl.lodz.p.it.spjava.e12.crs.dto;

import java.util.Date;
import javax.validation.constraints.Future;

/**
 *
 * @author marci
 */
public class ReservationDTO {

    private Long id;

    @Future
    private Date startDate;

    @Future
    private Date endDate;

    private boolean reservationConfirm;

    private int duration;

    public ReservationDTO() {
        startDate = new Date();  //toDo usunąć
        endDate = new Date();  //toDo usunąć
        reservationConfirm = false;  //toDo usunąć
        duration = 1;  //toDo usunąć
    }

    public ReservationDTO(Long id) {
        this.id = id;
    }

    public ReservationDTO(Long id, Date startDate, Date endDate, boolean reservationConfirm, int duration) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationConfirm = reservationConfirm;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isReservationConfirm() {
        return reservationConfirm;
    }

    public void setReservationConfirm(boolean reservationConfirm) {
        this.reservationConfirm = reservationConfirm;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
