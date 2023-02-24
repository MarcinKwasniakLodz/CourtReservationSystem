package pl.lodz.p.it.spjava.e12.crs.cdi.web.account;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ClientEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;

/**
 *
 * @author marci
 */
@Named(value = "clientController")
@SessionScoped
public class ClientController implements Serializable {

    //komponent CDI o zasięgu sesji, do utrzymywania stanu pola podczas edycji danych klienta
    @EJB
    private ClientEndpoint clientEndpoint;

    private ClientDTO clientDTO;

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) throws AppBaseException {
        clientEndpoint.loadClientInState(clientDTO);
        this.clientDTO = clientDTO;
    }

    public ClientController() {
    }

}
