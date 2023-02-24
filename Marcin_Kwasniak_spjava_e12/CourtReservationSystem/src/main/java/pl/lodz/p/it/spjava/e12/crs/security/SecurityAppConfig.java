package pl.lodz.p.it.spjava.e12.crs.security;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;


@FacesConfig
@ApplicationScoped
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/faces/login/customFormLogin.xhtml",
                errorPage = "/faces/login/customFormLoginError.xhtml",
                useForwardToLogin = false
        ))
public class SecurityAppConfig {

}
