package pl.lodz.p.it.spjava.e12.crs.security;

import javax.annotation.security.RunAs;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.e12.crs.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.e12.crs.model.Account;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RunAs("AUTHENTICATOR") //w ten sposób ten endpoint udostępnia metodę fasady niedostępną dla "zwykłych" ról
public class SecurityEndpoint {

    @Inject
    private AccountFacade accountFacade;

    // Odwołanie do metody fasady, która realizuje kwerendę sprawdzającą warunki poprawności uwierzytelniania
    // Pośrednictwo endpointa jest niezbędne ze względu na tworzenie kontekstu transakcyjnego
    public Account znajdzKontoSpelniajaceWarunkiUwierzytelnienia(String login, String skrotHasla) {
        return accountFacade.znajdzLoginISkrotHaslaWsrodAktywnychIPotwierdzonychKont(login, skrotHasla);
    }
}
