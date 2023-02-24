package pl.lodz.p.it.spjava.e12.crs.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author marci
 */
public class AccountDTO {

    private Long id;

    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    @Pattern(regexp = "^[_a-zA-Z0-9-]*$", message = "{constraint.string.incorrectchar}")
    @NotNull(message = "{constraint.notnull}")
    private String login;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 64, max = 64, message = "{constraint.string.length.notinrange}") //skrót SHA256 zawsze jest zapisywany w 64 znakach
    private String password;

    private boolean active;

    private boolean confirm;

    @NotNull(message = "{constraint.notnull}")
    private String kind;

    public AccountDTO() {
    }

    public AccountDTO(Long id) {
        this.id = id;
    }

    public AccountDTO(boolean active) {
        this.active = active;
    }

    public AccountDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AccountDTO(String login, String password, boolean active, boolean confirm, String kind) {
        this.login = login;
        this.password = password;
        this.active = active;
        this.confirm = confirm;
        this.kind = kind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
