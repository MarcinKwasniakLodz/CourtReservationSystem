package pl.lodz.p.it.spjava.e12.crs.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author marci
 */
@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "{constraint.notnull}")
    @Size(min = 64, max = 64, message = "{constraint.string.length.notinrange}") //skrót SHA256 zawsze jest zapisywany w 64 znakach
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    @Pattern(regexp = "^[_a-zA-Z0-9-]*$", message = "{constraint.string.incorrectchar}")
    @NotNull(message = "{constraint.notnull}")
    @Column(name = "login", length = 64)
    private String login;

    @Column(name = "confirm", nullable = false)
    private boolean confirm;

    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull(message = "{constraint.notnull}")
    @Column(name = "kind", updatable = false)
    private String kind;

    //@JoinColumn(name = "Client_ID", referencedColumnName = "ID")  //niepotrzebne, gdyż identywikacja połączenia jest już po drugiej stronie
    @OneToOne(mappedBy = "account")
    private Client client;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.spjava.e12.crs.model.Account[ id=" + id + " ]";
    }
}
