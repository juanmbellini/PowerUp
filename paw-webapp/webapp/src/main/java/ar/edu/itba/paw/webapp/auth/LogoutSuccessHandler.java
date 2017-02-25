package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.config.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Custom logout success handler that both doesn't redirect on success and adds CORS headers for local development.
 */
public class LogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public LogoutSuccessHandler() {
        super();    // Default is a 200 OK status.
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOG.info("{} logged out", authentication.getName());

        //CorsFilter does not apply here, add headers manually, required by CORS
        for (Map.Entry<String, String> header : CorsFilter.CORS_HEADERS.entrySet()) {
            response.setHeader(header.getKey(), header.getValue());
        }
        super.onLogoutSuccess(request, response, authentication);
    }
}
