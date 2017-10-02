package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Jwt;

import java.util.Calendar;
import java.util.List;

/**
 * Service layer for {@link ar.edu.itba.paw.webapp.model.Jwt JWTs}.
 */
public interface JwtService {

    /**
     * @return All JWTs.
     */
    List<Jwt> all();

    /**
     * Finds a JWT by ID.
     *
     * @param id The {@link Jwt}'s ID (its database ID, not the JWTID)
     * @return The matching JWT, or {@code null} if not present.
     */
    Jwt findById(long id);

    /**
     * Finds a JWT by its String representation.
     *
     * @param token The token's string representation (i.e. what the user sends in the Authorization header)
     * @return The matching JWT, or {@code null} if not present.
     */
    Jwt find(String token);

    /**
     * Blacklists a given token until its expiration date.
     *
     * @param tokenString The token's string representation (i.e. what the user sends in the Authorization header)
     * @param expirationDate The token's expiration date.
     * @return The blacklisted token, as represented in the database.
     */
    Jwt blacklist(String tokenString, Calendar expirationDate);

    /**
     * Checks whether a token is blacklisted.
     *
     * @param token The token's string representation (i.e. what the user sends in the Authorization header)
     * @return Whether the token is blacklisted.
     */
    boolean isBlacklisted(String token);

    /**
     * Checks whether a token is expired.
     *
     * @param token The token's string representation (i.e. what the user sends in the Authorization header)
     * @return Whether the token is expired.
     * @throws NoSuchEntityException If the token isn't blacklisted.
     */
    boolean isExpired(String token) throws NoSuchEntityException;

    /**
     * Deletes a JWT.
     *
     * @param token The token to delete.
     */
    void delete(Jwt token);
}
