package pl.lodz.p.it.spjava.e12.crs.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author marci
 */
public class ClientDTO {

    private Long id;

    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    @Pattern(regexp = "^[_a-zA-Z0-9-]*$", message = "{constraint.string.incorrectchar}")
    @NotNull(message = "{constraint.notnull}")
    private String login;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 6, max = 64, message = "{constraint.string.length.notinrange}")
    private String password;

    private boolean confirm;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    private String firstName;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    private String lastName;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 6, max = 64, message = "{constraint.string.length.notinrange}")
    @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$", message = "{constraint.string.incorrectemail}")
    private String email;

    @Size(min = 7, max = 12, message = "{constraint.string.length.notinrange}")
    @NotNull(message = "{constraint.notnull}")
    private String phoneNumber;

    private boolean active;

    @NotNull(message = "{constraint.notnull}")
    private String kind;

    public ClientDTO() {
    }

    public ClientDTO(Long id) {
        this.id = id;
    }

    public ClientDTO(String login, String password, boolean confirm, String firstName, String lastName, String email, String phoneNumber, boolean active, String kind) {
        this.login = login;
        this.password = password;
        this.confirm = confirm;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.kind = kind;
    }

    public ClientDTO(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public ClientDTO(Long id, String kind, String password, String login) {
        this.kind = kind;
        this.password = password;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
