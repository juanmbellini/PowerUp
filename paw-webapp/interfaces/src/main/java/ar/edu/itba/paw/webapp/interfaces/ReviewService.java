package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.model.Review;

import java.util.Set;

/**
 * Service layer for game reviews. Exposes functionality available to reviews.
 */
public interface ReviewService {

    /**
     * Creates a new review with the specified data.
     *
     * @param reviewerId The ID of the user who created the review.
     * @param gameId The ID of the game getting reviewed.
     * @param review The textual review.
     * @param storyScore The story score.
     * @param graphicsScore The graphics score.
     * @param audioScore The audio score.
     * @param controlsScore The controls score.
     * @param funScore The fun score.
     * @return The created review.
     * @throws NoSuchEntityException If no such user or game exists.
     * //TODO throw exception if said review already exists.
     */
    Review create(long reviewerId, long gameId, String review, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) throws NoSuchEntityException;

    /**
     * Returns a set of reviews for a specified game.
     *
     * @param id The ID of the game whose reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchGameException When an invalid game ID is provided.
     */
    Set<Review> findByGameId(long id) throws NoSuchGameException;

    /**
     * Returns a set of recent reviews, ordered by descending date, for a specified game, identified by ID.
     *
     * @param id The ID of the game whose reviews to fetch.
     * @param limit The maximum number of reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchGameException When an invalid game ID is provided.
     */
    Set<Review> findRecentByGameId(long id, int limit) throws NoSuchGameException;

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param id The ID of the user whose reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid user ID is provided.
     */
    Set<Review> findByUserId(long id) throws NoSuchUserException;

    /**
     * Returns a set of recent reviews, ordered by descending date, created by a specified user, identified by ID.
     *
     * @param id The ID of the user whose reviews to fetch.
     * @param limit The maximum number of reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid user ID is provided.
     */
    Set<Review> findRecentByUserId(long id, int limit) throws NoSuchUserException;

    /**
     * Returns a set of reviews created by a specified user.
     *
     * @param username The name of the user whose reviews to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid username is provided.
     */
    Set<Review> findByUsername(String username) throws NoSuchUserException;

    /**
     * Returns a set of recent reviews, ordered by descending date, created by a specified user, identified by username.
     *
     * @param username The username of the user whose reviews to fetch.
     * @param limit The maximum number of reviews to fetch.
     * @return The resulting set of reviews.
     * @throws NoSuchUserException When an invalid user title is provided.
     */
    Set<Review> findRecentByUsername(String username, int limit) throws NoSuchUserException;

    /**
     * Finds a review by ID.
     *
     * @param reviewId The ID to match.
     * @return The matching review or {@code null} if not found.
     */
    Review findById(long reviewId);

    /**
     * Finds a review given a user ID and user ID.
     *
     * @param userId The user ID.
     * @param gameId The game ID.
     * @return The matching review, or {@code null} if not found.
     * @throws NoSuchUserException If an invalid user ID is provided.
     * @throws NoSuchUserException If an invalid game ID is provided.
     */
    Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException;
}