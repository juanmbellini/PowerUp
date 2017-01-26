package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.config.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Failure handler for JSON-based authentication.
 *
 * @see JsonAuthenticationFilter
 */
public class JsonFailureHandler implements AuthenticationFailureHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //CorsFilter does not apply here, add headers manually, required by CORS
        for (Map.Entry<String, String> header : CorsFilter.CORS_HEADERS.entrySet()) {
            response.setHeader(header.getKey(), header.getValue());
        }

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
