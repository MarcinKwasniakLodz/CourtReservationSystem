package pl.lodz.p.it.spjava.e12.crs.cdi.web.account;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.e12.crs.dto.AccountDTO;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ClientEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import pl.lodz.p.it.spjava.e12.crs.security.HashGenerator;
import pl.lodz.p.it.spjava.e12.crs.utils.ContextUtils;

/**
 *
 * @author marci
 */
@Named(value = "createClientPageBean")
@SessionScoped
public class CreateClientPageBean implements Serializable {

    @EJB
    private ClientEndpoint clientEndpoint;

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private HashGenerator hashGenerator;

    private ClientDTO createClientDTO;

    public ClientDTO getCreateClientDTO() {
        return createClientDTO;
    }

    public void setCreateClientDTO(ClientDTO createClientDTO) {
        this.createClientDTO = createClientDTO;
    }

    public CreateClientPageBean() {

    }

    private ClientDTO client = new ClientDTO();

    private AccountDTO account = new AccountDTO();

    public ClientEndpoint getClientEndpoint() {
        return clientEndpoint;
    }

    public void setClientEndpoint(ClientEndpoint clientEndpoint) {
        this.clientEndpoint = clientEndpoint;
    }

    public AccountEndpoint getAccountEndpoint() {
        return accountEndpoint;
    }

    public void setAccountEndpoint(AccountEndpoint accountEndpoint) {
        this.accountEndpoint = accountEndpoint;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    @PostConstruct
    private void init() {
        createClientDTO = new ClientDTO();
    }

    private String hasloPowtorz = "";

    public String getHasloPowtorz() {
        return hasloPowtorz;
    }

    public void setHasloPowtorz(String hasloPowtorz) {
        this.hasloPowtorz = hasloPowtorz;
    }

    public String actionApproveRegisterClient() {

        if (!(hasloPowtorz.equals(createClientDTO.getPassword()))) {
            ContextUtils.emitInternationalizedMessage("createClient:powtorzHaslo", "passwords.not.matching");
            return null;
        }
        return "confirmClientSuccess";
    }

    public String actionConfirmRegisterClient() throws AppBaseException {
        createClientDTO.setPassword(hashGenerator.generateHash(createClientDTO.getPassword()));
        clientEndpoint.createClient(createClientDTO);
        //gdy metoda zwraca null to pozostaje na tej samej stronie, ta ma iść na listę klientów - jest to ustwione w faces-config.xml;
        return "createClientSuccess";
    }

}
