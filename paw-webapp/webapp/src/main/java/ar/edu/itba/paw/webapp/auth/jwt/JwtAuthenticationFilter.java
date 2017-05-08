package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

/**
 * JWT authentication filter. Applied on all pages for stateless API traversing (i.e. JWT token must be provided for
 * each API request that requires authentication).
 *
 * @see <a href="https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java">Source</a>
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final RequestMatcher logOutRequestMatcher = new AntPathRequestMatcher("/api/auth/logout", HttpMethod.POST);

    @Autowired
    private JwtService jwtService;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        JwtAuthenticationToken requestToken = extractToken(request);
        if (requestToken == null) {
            throw new MissingJwtException("No JWT token found in request headers");
        }
        return getAuthenticationManager().authenticate(requestToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        //Successfully authenticated against logout endpoint. Invalidate JWT.
        if(logOutRequestMatcher.matches(request)) {
            JwtAuthenticationToken token = extractToken(request);
            jwtService.blacklist(token.getToken(), null);   //TODO set expiration date on JWTs and use it here
        }

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

    private JwtAuthenticationToken extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String authToken = header.substring(7);
        return new JwtAuthenticationToken(authToken);
    }
}