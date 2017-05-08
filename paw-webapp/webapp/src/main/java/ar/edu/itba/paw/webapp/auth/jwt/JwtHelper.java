package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

/**
 * JWT utility class used for creating and validating JWTs.
 *
 * @see <a href="https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java">Source</a>
 */
@Component
public class JwtHelper {

    private final String secret;

    private final SecureRandom saltGenerator = new SecureRandom();

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    public JwtHelper(Environment environment) {
        this.secret = environment.getRequiredProperty("jwt.secret");
    }

    /**
     * Generates a JWT containing necessary information to uniquely identify the provided {@link User}.
     *
     * @param u The user for which the token will be generated.
     * @return The JWT token.
     */
    public String generateToken(User u) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setHeaderParam("salt", saltGenerator.nextLong())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Tries to parse specified String as a JWT. If successful, returns the username this JWT belongs to.
     * If unsuccessful (token is invalid or does not contain all required user properties), returns {@code null}.
     *
     * @param tokenString the JWT to parse.
     * @return The data extracted from the specified token or {@code null} if the token is invalid.
     */
    public String parseToken(String tokenString) {
        try {
            Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
            Claims body = (Claims) token.getBody();
            Header header = token.getHeader();

            if(body.getIssuedAt() == null || body.getIssuedAt().after(new Date())) {
                throw new JwtException("Invalid or nonexistent issued_at date, rejecting");
            }
            if(header.get("salt") == null) {
                throw new JwtException("No salt present in JWT, rejecting");
            }

            return body.getSubject();
        } catch (JwtException | ClassCastException e) {
            LOG.warn("Error validating JWT: {}", e);
            return null;
        }
    }
}