package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Set;

/**
 * Data Access Object for game reviews.
 */
public interface ReviewDao {

    /**
     * @see ReviewService#findByGameId(long)
     */
    Set<Review> findByGameId(long id) throws NoSuchGameException;

    /**
     * @see ReviewService#findByGameName(String)
     */
    Set<Review> findByGameName(String name) throws NoSuchGameException;

    /**
     * @see ReviewService#findByUserId(long)
     */
    Set<Review> findByUserId(long id) throws NoSuchUserException;

    /**
     * @see ReviewService#findByUserName(String)
     */
    Set<Review> findByUserName(String username) throws NoSuchUserException;

    /**
     * @see ReviewService#findById(long)
     */
    Review findById(long reviewId);

    /**
     * @see ReviewService#find(long, long)
     */
    Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException;
}
