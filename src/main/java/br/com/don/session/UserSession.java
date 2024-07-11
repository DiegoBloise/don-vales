package br.com.don.session;

import java.io.IOException;
import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;

import br.com.don.model.Usuario;
import br.com.don.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

@Data
@Named
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    @Inject
    private UsuarioRepository repository;


    @PostConstruct
    void init() {

    }


    public void doLogin(String username, String password) throws IOException {

        Boolean usuarioOk = false;
        Boolean senhaOk = false;

        this.usuario = repository.findByProperty("username", username);

        if (usuario != null) {
            usuarioOk = true;
            senhaOk = BCrypt.checkpw(password, usuario.getPassword());
        }

        if (usuarioOk && senhaOk) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            HttpSession session = (HttpSession) externalContext.getSession(true);
            session.setAttribute("username", usuario.getUsername());

            //cookieHelper.setCookie("username", usuario.getUsername(), 30 * 60);

            externalContext.redirect(externalContext.getRequestContextPath() + "/app/index.xhtml");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Login inválido:", "Verifique seu nome de usuário e senha."));
        }
    }


    public void doLogout() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        //cookieHelper.setCookie("username", usuario.getUsername(), 0);

        externalContext.invalidateSession();

        externalContext.redirect(externalContext.getRequestContextPath() + "/auth/login.xhtml");
    }


    @Override
    public String toString() {
        return "UserSession [usuario=" + usuario + "]";
    }
}
