package ar.edu.itba.paw.webapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Failure handler for JSON-based authentication.
 *
 * @see JsonAuthenticationFilter
 */
public class JsonFailureHandler implements AuthenticationFailureHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Redirect the user to the URL they were originally attempting to access, or to wherever was specified in
     * {@link JsonAuthenticationFilter}.
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(request.getAttribute("redirect").toString());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LoginDto loginRequest = (LoginDto) request.getAttribute("loginRequest");

        if(exception instanceof JsonAuthenticationFilter.AlreadyLoggedInException) {
            LOG.warn("Logged-in user attempted to log in again, why isn't Spring blocking this?");
            response.setStatus(404);
            return;
        }
        if(loginRequest == null) {  //Sent something that isn't valid JSON or couldn't be mapped to a LoginDto
            LOG.info("Invalid login request: {}", exception.getCause().getMessage());    //Cause shouldn't be null here
            response.setStatus(400);
            return;
        }
        if(exception instanceof BadCredentialsException) {
            if(loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                LOG.info("Invalid login JSON");
                response.setStatus(422);    //TODO use GitHub-like procedures to inform user what was missing
            } else {
                LOG.info("Invalid login credentials");
                response.setStatus(401);
            }
        }
    }
}
