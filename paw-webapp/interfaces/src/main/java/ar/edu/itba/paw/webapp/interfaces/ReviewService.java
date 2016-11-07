package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Set;

/**
 * Service layer for game reviews. Exposes functionality available to reviews.
 */
public interface ReviewService {

    /**
     * Returns a set of reviews for a specified game.
     *
     * @param id The ID of the game whose reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchGameException When an invalid game ID is provided.
     */
    Set<Review> findByGameId(long id) throws NoSuchGameException;

    /**
     * Returns a set of reviews for a specified game.
     *
     * @param name The name of the game whose reviews to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of reviews.
     * @throws NoSuchGameException When an invalid game name is provided.
     */
    Set<Review> findByGameName(String name) throws NoSuchGameException;

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param id The ID of the user whose reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid user ID is provided.
     */
    Set<Review> findByUserId(long id) throws NoSuchUserException;

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param username The name of the user whose reviews to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid username is provided.
     */
    Set<Review> findByUserName(String username) throws NoSuchUserException;

    /**
     * Finds a review by ID.
     *
     * @param reviewId The ID to match.
     * @return The matching review or {@code null} if not found.
     */
    Review findById(long reviewId);

    /**
     * Finds a review given a game ID and user ID.
     *
     * @param userId The user ID.
     * @param gameId The game ID.
     * @return The matching review, or {@code null} if not found.
     * @throws NoSuchUserException If an invalid user ID is provided.
     * @throws NoSuchUserException If an invalid game ID is provided.
     */
    Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException;
}