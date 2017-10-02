package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.config.CorsFilter;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Failure handler for JWT-based authentication.
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(CorsFilter.isEnabled) {
            for (Map.Entry<String, String> header : CorsFilter.CORS_HEADERS.entrySet()) {
                response.setHeader(header.getKey(), header.getValue());
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(ContentType.TEXT_HTML.toString());
        response.setContentLength(0);
    }
}