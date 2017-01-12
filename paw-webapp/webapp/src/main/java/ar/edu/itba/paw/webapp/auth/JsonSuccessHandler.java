package ar.edu.itba.paw.webapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Success handler for JSON-based authentication.
 *
 * @see JsonAuthenticationFilter
 */
public class JsonSuccessHandler implements AuthenticationSuccessHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Redirect the user to the URL they were originally attempting to access, or to wherever was specified in
     * {@link JsonAuthenticationFilter}.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirect = ((LoginDto) request.getAttribute("loginRequest")).getRedirect();
        LOG.info("{} logged in, redirecting to {}", authentication.getName(), redirect);
        response.sendRedirect(redirect);
    }
}
