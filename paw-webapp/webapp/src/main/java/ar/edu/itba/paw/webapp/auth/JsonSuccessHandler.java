package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.config.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Success handler for JSON-based authentication.
 *
 * @see JsonAuthenticationFilter
 */
public class JsonSuccessHandler implements AuthenticationSuccessHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Send a 200 OK.
     *
     * @see JsonAuthenticationFilter
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOG.info("{} logged in", authentication.getName());

        //CorsFilter does not apply here, add headers manually, required by CORS
        for (Map.Entry<String, String> header : CorsFilter.CORS_HEADERS.entrySet()) {
            response.setHeader(header.getKey(), header.getValue());
        }
        response.setStatus(200);
    }
}
