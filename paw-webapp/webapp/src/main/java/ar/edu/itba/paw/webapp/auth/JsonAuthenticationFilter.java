package ar.edu.itba.paw.webapp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Attempts authentication from a JSON payload. Additionally attaches the attempted login request to the HTTP request
     * (<b>not</b> the response, since we don't know what kind of response this attempt will result in) for either the
     * {@link JsonSuccessHandler} or {@link JsonFailureHandler} to use as appropriate.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*
         * Saving username and password as instance variables here is thread-unsafe (this class will
         * later be used as a bean), so instead we're taking the approach suggested by
         * http://stackoverflow.com/a/20353336/2333689
         */
        LoginDto login;
        try {
            login = getLoginRequest(request);
            request.setAttribute("loginRequest", login);
        } catch (IOException e) {
            throw new InvalidLoginRequestException("Couldn't process login request", e);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

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
}
