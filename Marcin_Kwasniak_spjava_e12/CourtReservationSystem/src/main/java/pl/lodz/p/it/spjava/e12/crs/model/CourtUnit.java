package pl.lodz.p.it.spjava.e12.crs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 *
 * @author marci
 */
@NamedQueries({
    @NamedQuery(name = "CourtUnit.findByCourtName", query = "SELECT t FROM CourtUnit t WHERE t.courtName = :courtName")
})

@Entity
public class CourtUnit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;   // nr wersji potrzebny do mechanizmu blokad optymistycznych

    @Column(unique = true, nullable = false, updatable = false)
    private Short courtNumber;

    @Column(unique = true, nullable = false, length = 30)
    private String courtName;

    @Column(nullable = false, updatable = false)
    private String typeOfCourt;

    @Column(nullable = false, updatable = false)
    private String typeOfSurface;

    @Column()
    public boolean available;

    @OneToMany(mappedBy = "courtUnit", cascade = CascadeType.ALL)
    private List<Reservation> listReservation = new ArrayList<>();

    public List<Reservation> getListReservation() {
        return listReservation;
    }

    public void setListReservation(List<Reservation> listReservation) {
        this.listReservation = listReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getCourtNumber() {
        return courtNumber;
    }

    public void setCourtNumber(Short courtNumber) {
        this.courtNumber = courtNumber;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getTypeOfCourt() {
        return typeOfCourt;
    }

    public void setTypeOfCourt(String typeOfCourt) {
        this.typeOfCourt = typeOfCourt;
    }

    public String getTypeOfSurface() {
        return typeOfSurface;
    }

    public void setTypeOfSurface(String typeOfSurface) {
        this.typeOfSurface = typeOfSurface;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourtUnit)) {
            return false;
        }
        CourtUnit other = (CourtUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.spjava.e12.crs.model.CourtUnit[ id=" + id + " ]";
    }
}
