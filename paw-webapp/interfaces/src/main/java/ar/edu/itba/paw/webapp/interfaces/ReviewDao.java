package ar.edu.itba.paw.webapp.interfaces;

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
    Set<Review> findByGameId(long id);

    /**
     * @see ReviewService#findByGameName(String)
     */
    Set<Review> findByGameName(String name);

    /**
     * @see ReviewService#findByUserId(long)
     */
    Set<Review> findByUserId(long id);

    /**
     * @see ReviewService#findByUserName(String)
     */
    Set<Review> findByUserName(String name);

    /**
     * @see ReviewService#findById(long)
     */
    Review findById(long reviewId);

    /**
     * @see ReviewService#exists(Game, User)
     */
    boolean exists(Game game, User user);
}
