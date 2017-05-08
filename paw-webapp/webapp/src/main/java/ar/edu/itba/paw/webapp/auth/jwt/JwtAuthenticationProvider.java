package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * JWT-based authentication provider. Couples JWT with Spring Security's authentication process.
 *
 * @see <a href="https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java">Source</a>
 */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if(!(authentication instanceof JwtAuthenticationToken)) {
            throw new MalformedJwtException("Didn't receive JwtAuthenticationToken in JwtAuthenticationProvider, rejecting authentication");
        } else if(jwtService.isBlacklisted(((JwtAuthenticationToken) authentication).getToken())) {
            throw new BlacklistedJwtException("Attempted authenticating with a blacklisted JWT, rejecting authentication");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        String authenticatedUsername = jwtHelper.parseToken(token);
        if (authenticatedUsername == null) {
            throw new MalformedJwtException("JWT token is invalid");
        }
        return userDetailsService.loadUserByUsername(authenticatedUsername); //TODO configure user details service from outside?
    }

}