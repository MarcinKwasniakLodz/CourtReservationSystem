package pl.lodz.p.it.spjava.e12.crs.dto;

/**
 *
 * @author marci
 */
public class CourtUnitDTO {

    private Long id;

    private Short courtNumber;

    private String courtName;

    private String typeOfCourt;

    private String typeOfSurface;

    private boolean available;

    public CourtUnitDTO() {
    }

    public CourtUnitDTO(Long id) {
        this.id = id;
    }

// konstruktor dla wszystkich pół
    public CourtUnitDTO(Long id, Short courtNumber, String courtName, String typeOfCourt, String typeOfSurface, boolean available) {
        this.id = id;
        this.courtNumber = courtNumber;
        this.courtName = courtName;
        this.typeOfCourt = typeOfCourt;
        this.typeOfSurface = typeOfSurface;
        this.available = available;
    }

    public CourtUnitDTO(Long id, String courtName, Short courtNumber, String typeOfCourt, String typeOfSurface) {
        this.id = id;
        this.courtNumber = courtNumber;
        this.courtName = courtName;
        this.typeOfCourt = typeOfCourt;
        this.typeOfSurface = typeOfSurface;
    }

    public Long getId() {
        return id;
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

    // metoda zwracająca courtName, potrzebne do wyświetlania listy na stronie po nazwie kortu
    @Override
    public String toString() {
        return "CourtUnitDTO{" + "id=" + id + ", courtNumber=" + courtNumber + ", courtName=" + courtName + ", typeOfCourt=" + typeOfCourt + ", typeOfSurface=" + typeOfSurface + ", available=" + available + '}';
    }
}
