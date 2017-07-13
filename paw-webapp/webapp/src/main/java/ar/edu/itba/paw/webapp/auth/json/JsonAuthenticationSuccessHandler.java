package ar.edu.itba.paw.webapp.auth.json;

import ar.edu.itba.paw.webapp.auth.jwt.JwtHelper;
import ar.edu.itba.paw.webapp.config.CorsFilter;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Success handler for JSON-based initial authentication. Attaches a JWT for further authentication.
 */
@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    /**
     * Attach a JWT to the 200 OK and write the authenticated user's JSON to the response.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOG.info("{} authenticated, attaching JWT", authentication.getName());

        response.addHeader("X-TOKEN", jwtHelper.generateToken(userService.findByUsername(authentication.getName())));

        //CorsFilter does not apply here, add headers manually, required by CORS
        if(CorsFilter.isEnabled) {
            for (Map.Entry<String, String> header : CorsFilter.CORS_HEADERS.entrySet()) {
                response.setHeader(header.getKey(), header.getValue());
            }
        }
        response.setStatus(200);

        //Successfully logged in, send full user JSON in response
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        new ObjectMapper().writeValue(response.getOutputStream(), new UserDto(userService.findByUsername(authentication.getName())));
    }
}
