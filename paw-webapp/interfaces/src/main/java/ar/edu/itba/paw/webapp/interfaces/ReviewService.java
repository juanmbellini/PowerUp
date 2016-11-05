package ar.edu.itba.paw.webapp.interfaces;

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
     */
    Set<Review> findByGameId(long id);

    /**
     * Returns a set of reviews for a specified game.
     *
     * @param name The name of the game whose reviews to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of reviews.
     */
    Set<Review> findByGameName(String name);

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param id The ID of the user whose reviews to fetch.
     * @return The resulting set of reviews.
     */
    Set<Review> findByUserId(long id);

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param name The name of the user whose reviews to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of reviews.
     */
    Set<Review> findByUserName(String name);

    /**
     * Finds a review by ID.
     *
     * @param reviewId The ID to match.
     * @return The matching review or {@code null} if not found.
     */
    Review findById(long reviewId);

    /**
     * Checks whether a review created by a specified user for a specified game exists.
     *
     * @param game The game the review was created for.
     * @param user The user who created the review.
     * @return Whether such a review exists.
     */
    boolean exists(Game game, User user);
}