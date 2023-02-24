package pl.lodz.p.it.spjava.e12.crs.cdi.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.e12.crs.dto.ClientDTO;
import pl.lodz.p.it.spjava.e12.crs.ejb.endpoints.ClientEndpoint;
import pl.lodz.p.it.spjava.e12.crs.exception.AppBaseException;
import static pl.lodz.p.it.spjava.e12.crs.exception.KontoException.exceptionRequiredDataIsNotAvailable;
import pl.lodz.p.it.spjava.e12.crs.utils.ContextUtils;

/**
 *
 * @author marci
 */
@Named(value = "editClientPageBean")
@RequestScoped
public class EditClientPageBean {

    @EJB
    private ClientEndpoint clientEndpoint;

    @Inject
    private ClientController clientController;

    private ClientDTO clientDTO;

    private ClientDTO savedClientDTO;

    public EditClientPageBean() {       // konstruktor bezparametrowy
    }

    public ClientDTO getSavedClientDTO() {
        return savedClientDTO;
    }

    public void setSavedClientDTO(ClientDTO savedClientDTO) {
        this.savedClientDTO = savedClientDTO;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    // metoda pobierający obiekt (dane klienta)z backendu, aby poniższa metoda akcji (saveClientAction) mogła zapisać zmiany
    @PostConstruct
    private void init() {
        clientDTO = clientController.getClientDTO();        // wczytanie danych z pola kontrolera - które trafią do formularza edycji WEB
        savedClientDTO = new ClientDTO();       //pole do zapisu zmian podanych w formularzu !!!
    }

    // metoda akcji która zapisze zmieniany obiekt (dane klienta), który został pobrany z backendu przez metodę init() (ta powyżej)
    public String saveClientAction() throws AppBaseException, Exception {
        if (null == clientDTO) {
            throw exceptionRequiredDataIsNotAvailable();
        }
        try {
            savedClientDTO = clientController.getClientDTO();        //pole do zapisu zmian podanych w formularzu !!!

            clientEndpoint.editClient(clientDTO);
            return "editClientSuccess";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(EditClientPageBean.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji wyjatku: ", ex);
            ContextUtils.emitInternationalizedMessageOfException(ex);
            return null;

        }
    }
}
