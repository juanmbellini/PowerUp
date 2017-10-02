package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;

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

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RequestMatcher optionallyAuthenticatedEndpointsMatcher;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        JwtAuthenticationToken requestToken = extractToken(request);
        if (requestToken == null) {
            if (optionallyAuthenticatedEndpointsMatcher.matches(request)) {
                // Return anonymous authentication (i.e. not logged in)
                return new AnonymousAuthenticationToken("PAW_ANONYMOUS", "ANONYMOUS", Collections.singletonList(new SimpleGrantedAuthority("NONE")));
            } else {
                throw new MissingJwtException("No JWT token found in request headers");
            }
        }
        return getAuthenticationManager().authenticate(requestToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        //Successfully authenticated against logout endpoint. Invalidate JWT.
        if(logOutRequestMatcher.matches(request)) {
            String tokenString = extractToken(request).getToken();
            Claims tokenClaims = extractTokenClaims(request);
            Calendar expiry = Calendar.getInstance();
            expiry.setTime(tokenClaims.getExpiration());
            jwtService.blacklist(tokenString, expiry);
        }

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

    /**
     * Extracts a JWT received in a particular request as a string, just as the user sent it.
     *
     * @param request The request to process.
     * @return The received JWT, or {@code null} if not present.
     */
    private JwtAuthenticationToken extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String authToken = header.substring(7);
        return new JwtAuthenticationToken(authToken);
    }

    /**
     * Extracts the claims from a JWT received in a particular request as a string, just as the user sent it.
     *
     * @param request The request to process.
     * @return The received JWT's claims, or {@code null} if not present.
     */
    private Claims extractTokenClaims(HttpServletRequest request) {
        JwtAuthenticationToken token = extractToken(request);
        if(token == null) {
            return null;
        }
        return jwtHelper.parseToken(token.getToken());
    }
}