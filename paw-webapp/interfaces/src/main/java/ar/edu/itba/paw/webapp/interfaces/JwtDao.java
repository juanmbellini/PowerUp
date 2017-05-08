package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Jwt;

import java.util.Calendar;

/**
 * DAO for {@link Jwt JWTs}.
 */
public interface JwtDao {

    /**
     * @see JwtService#findById(long)
     */
    Jwt findById(long id);

    /**
     * @see JwtService#find(String)
     */
    Jwt find(String token);

    /**
     * @see JwtService#blacklist(String, Calendar)
     */
    Jwt blacklist(String tokenString, Calendar expirationDate);

    /**
     * @see JwtService#isBlacklisted(String)
     */
    boolean isBlacklisted(String token);

    /**
     * @see JwtService#isExpired(String)
     */
    boolean isExpired(String token) throws NoSuchEntityException;
}
