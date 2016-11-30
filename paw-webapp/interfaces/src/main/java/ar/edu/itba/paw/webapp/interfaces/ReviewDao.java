package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Set;

/**
 * Data Access Object for game reviews.
 */
public interface ReviewDao {

    /**
     * @see ReviewService#create(long, long, String, int, int, int, int, int) 
     */
    Review create(long reviewerId, long gameId, String review, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) throws NoSuchEntityException;

    /**
     * @see ReviewService#findByGameId(long)
     */
    Set<Review> findByGameId(long id) throws NoSuchGameException;

    /**
     * @see ReviewService#findPageByGameId(long, int, int)
     */
    Page<Review> findPageByGameId(long id, int pageNumber, int pageSize);

    /**
     * @see ReviewService#findRecentByGameId(long, int)
     */
    Set<Review> findRecentByGameId(long id, int limit) throws NoSuchGameException;

    /**
     * @see ReviewService#findByUserId(long)
     */
    Set<Review> findByUserId(long id) throws NoSuchUserException;

    /**
     * @see ReviewService#findPageByUserId(long, int, int)
     */
    Page<Review> findPageByUserId(long id, int pageNumber, int pageSize);

    /**
     * @see ReviewService#findRecentByUserId(long, int)
     */
    Set<Review> findRecentByUserId(long id, int limit) throws NoSuchUserException;

    /**
     * @see ReviewService#findByUsername(String)
     */
    Set<Review> findByUsername(String username) throws NoSuchUserException;

    /**
     * @see ReviewService#findRecentByUsername(String, int)
     */
    Set<Review> findRecentByUsername(String username, int limit) throws NoSuchUserException;

    /**
     * @see ReviewService#findById(long)
     */
    Review findById(long reviewId);

    /**
     * @see ReviewService#find(long, long)
     */
    Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException;

    /**
     * @see ReviewService#delete(long)
     */
    void delete(long reviewId);
}
