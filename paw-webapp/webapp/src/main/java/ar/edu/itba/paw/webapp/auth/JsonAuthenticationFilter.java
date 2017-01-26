package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.interfaces.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JSON-based username-password authentication filter.
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionService sessionService;

    /**
     * Attempts authentication from a JSON payload.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*
         * Saving username and password as instance variables here is not thread-safe (this class will later be used as
         * a bean), so instead we're taking the approach suggested by http://stackoverflow.com/a/20353336/2333689
         */
        LoginDto login;
        try {
            if(sessionService.isLoggedIn()) {
                throw new AlreadyLoggedInException(sessionService.getCurrentUsername() + " is already logged in");
            }
            login = getLoginRequest(request);
            request.setAttribute("loginRequest", login);
        } catch (IOException e) {
            throw new InvalidLoginRequestException("Couldn't process login request", e);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

  /**
   * Parses the JSON payload from a login request and attempts to build a POJO with the appropriate data.
   *
   * @param request The HTTP request.
   * @return The login credentials as a {@link LoginDto}
   * @throws IOException If an I/O or JSON error occurs.
   */
    private LoginDto getLoginRequest(HttpServletRequest request) throws IOException {
        //Get JSON
        String body = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        //Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        LoginDto loginRequest = mapper.readValue(body, LoginDto.class);
        return loginRequest;
    }

    /**
     * Exception thrown when a parsing error occurs in an authentication attempt.
     */
    public class InvalidLoginRequestException extends AuthenticationException {

        public InvalidLoginRequestException(String msg, Throwable t) {
            super(msg, t);
        }

        public InvalidLoginRequestException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception thrown when a user that is already logged attempts to log in again (TODO why won't Spring block this?)
     */
    public class AlreadyLoggedInException extends AuthenticationException {

        public AlreadyLoggedInException(String msg, Throwable t) {
            super(msg, t);
        }

        public AlreadyLoggedInException(String msg) {
            super(msg);
        }
    }
}
