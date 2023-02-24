package pl.lodz.p.it.spjava.e12.crs.cdi.web.account;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ClientEndpoint;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;

/**
 *
 * @author marci
 */
@Named(value = "listClientPageBean")
@RequestScoped
public class ListClientPageBean implements Serializable {      //w tej klasie pobieram listę klientów z DTO i udostępniam ją dla widoku

    @EJB
    private ClientEndpoint clientEndpoint;

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private ClientController clientController;

    @EJB
    private AccountFacade accountFacade;

    private List<ClientDTO> listClientDTO;

    public List<ClientDTO> getListClientDTO() {
        return listClientDTO;
    }

    public void setListClientDTO(List<ClientDTO> listClientDTO) {
        this.listClientDTO = listClientDTO;
    }

    public ListClientPageBean() {
    }

    @PostConstruct
    private void init() {
        listClientDTO = clientEndpoint.listAllClient();
    }

    public String deleteClientAction(ClientDTO clientDTO) throws AppBaseException {
        clientEndpoint.removeClient(clientDTO);

        return "cancelAction";
    }

    // metoda zaczynająca tryb edycji klienta - naciśnięcie przycisku edit na liście klientów
    public String editClientAction(ClientDTO clientDTO) throws AppBaseException {
        clientController.setClientDTO(clientDTO);
        return "editClient";
    }

    public String activateClientAction(ClientDTO clientDTO) throws AppBaseException {
        clientEndpoint.activateClient(clientDTO);
        return "editClientSuccess";
    }

    public String deactivateClientAction(ClientDTO clientDTO) throws AppBaseException {
        clientEndpoint.deactivateClient(clientDTO);
        return "editClientSuccess";
    }

    public String confirmClientAction(ClientDTO clientDTO) throws AppBaseException {
        clientEndpoint.confirmClient(clientDTO);
        return "editClientSuccess";
    }

}
