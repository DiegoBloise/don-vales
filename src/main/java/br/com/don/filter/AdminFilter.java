package br.com.don.filter;

import java.io.IOException;

import br.com.don.session.UserSession;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Inject
    private UserSession userSession;


    public void init(FilterConfig fConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

            if (userSession.getUsuario() != null) {

                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                HttpSession session = httpRequest.getSession();
                session.setMaxInactiveInterval(30 * 60);

                if (userSession.getUsuario().isAdmin()) {
                    chain.doFilter(request, response);
                } else {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/access.xhtml");
                }
            } else {
                chain.doFilter(request, response);
            }
    }


    public void destroy() {

    }
}
