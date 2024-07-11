package br.com.don.view.auth;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;

@Data
@Named
@SessionScoped
public class AuthView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String authKey = "321654";

    private String password;

    private Boolean isAdmin;

    private Integer loginAttemptsCounter;


    @PostConstruct
    public void init() {
        authKey = authKey + LocalDate.now().getDayOfMonth();

        isAdmin = false;

        password = "";

        loginAttemptsCounter = 0;
    }


    public void setUser() throws IOException {

        if (password.equals(authKey) && !isAdmin) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Autorizado Com Sucesso", ""));
            isAdmin = true;
        } else if (isAdmin) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bloqueado com Sucesso", ""));
            isAdmin = false;

            /* FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/index.xhtml"); */
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Credenciais Inválidas", "This action will be reported to the admin."));
            isAdmin = false;
            loginAttemptsCounter++;
            System.out.println("*******\n\nTentativa de Login N# " + loginAttemptsCounter + " Registrada: " + LocalDateTime.now() + "\n\n*******");
        }

        password = "";
    }
}