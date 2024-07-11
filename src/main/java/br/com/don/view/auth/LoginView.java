package br.com.don.view.auth;

import java.io.IOException;
import java.io.Serializable;

import br.com.don.session.UserSession;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

@Data
@Named
@ViewScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    @Inject
    private UserSession userSession;


    public void doLogin() throws IOException {
        userSession.doLogin(username, password);
    }
}