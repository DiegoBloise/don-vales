package br.com.don.filter;

import java.io.IOException;

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

@WebFilter(urlPatterns = {"/admin/*", "/app/*", "/auth/*"}) // Remove comment to enable the filter
public class AuthenticationFilter implements Filter {


    public void init(FilterConfig fConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession();
        session.setMaxInactiveInterval(6 * 60 * 60);

        String username = (String) session.getAttribute("username");
        boolean isLoggedIn = username != null;

        boolean isLoginPage = httpRequest.getRequestURI().endsWith("login.xhtml");

        if (isLoggedIn) {
            if (isLoginPage) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/app/index.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (isLoginPage) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login.xhtml");
            }
        }
    }


    public void destroy() {

    }
}
