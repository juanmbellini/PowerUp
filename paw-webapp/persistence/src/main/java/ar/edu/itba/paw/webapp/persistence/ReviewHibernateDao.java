package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by juanlipuma on Nov/7/16.
 */
@Repository
public class ReviewHibernateDao implements ReviewDao {

    //TODO fix repeated code, extract some finding functionality to a generalized method if possible

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;

    private final UserDao userDao;

    @Autowired
    private ReviewHibernateDao(GameDao gameDao, UserDao userDao) {
        this.gameDao = gameDao;
        this.userDao = userDao;
    }


    @Override
    public Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String userNameFilter,
                                   int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {
        if ((gameIdFilter != null && gameIdFilter <= 0) || (userIdFilter != null && userIdFilter <= 0)
                || userNameFilter != null && userNameFilter.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Game id filter
        String gameIdFilterString = "";
        if (gameIdFilter == null) {
            gameIdFilterString = "true = true";
        } else {
            gameIdFilterString = "game.id = :gameId";
        }
        // Game name filter
        String gameNameFilterString = "";
        if (gameNameFilter == null) {
            gameNameFilterString = "true = true";
        } else {
            gameNameFilterString = "game.name = :gameName";
        }
        // User id filter
        String userIdFilterString = "";
        if (userIdFilter == null) {
            userIdFilterString = "true = true";
        } else {
            userIdFilterString = "user.id = :userId";
        }
        // User name filter
        String userNameFilterString = "";
        if (userNameFilter == null) {
            userNameFilterString = "true = true";
        } else {
            userNameFilterString = "user.username = :userName";
        }

        return DaoHelper.findPageWithConditions(em, Review.class, pageNumber, pageSize,
                "FROM Review WHERE " + gameIdFilterString + " AND " + gameNameFilterString +
                        " AND " + userIdFilterString + " AND " + userNameFilterString +
                        " ORDER BY " + sortingType.getFieldName() + " " + sortDirection.getQLKeyword(),
                parameters(gameIdFilter, gameNameFilter, userIdFilter, userNameFilter));
    }

    @Override
    public Review create(User reviewer, Game game,
                         String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                         Integer controlsScore, Integer funScore) {
        final Review review =
                new Review(reviewer, game, reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
        em.persist(review);
        return review;
    }

    @Override
    public void update(Review review,
                       String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                       Integer controlsScore, Integer funScore) {
        if (review == null) {
            throw new IllegalArgumentException();
        }
        review.update(reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
        em.merge(review);
    }

    @Override
    public void delete(Review review) {
        if (review == null) {
            throw new IllegalArgumentException();
        }
        em.remove(review);
    }

    @Override
    public Review findById(long reviewId) {
        return em.find(Review.class, reviewId);
    }

    @Override
    @Deprecated
    public Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException {
        if (!userDao.existsWithId(userId)) {
            throw new NoSuchUserException(userId);
        }
        if (!gameDao.existsWithId(gameId)) {
            throw new NoSuchGameException(gameId);
        }
        return DaoHelper.findSingleWithConditions(em, Review.class, "FROM Review AS R WHERE R.game.id = ?1 AND R.user.id = ?2", gameId, userId);
    }


    /**
     * Creates the array of parameters to be used in the
     * {@link #getReviews(Long, String, Long, String, int, int, SortingType, SortDirection)} method.
     *
     * @param gameIdFilter   The game id filter.
     * @param gameNameFilter The game name filter.
     * @param userIdFilter   The user id filter.
     * @param userNameFilter The username filter.
     * @return The array of parameters.
     */
    private Object[] parameters(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String userNameFilter) {
        List<Object> parameters = new LinkedList<>();
        if (gameIdFilter != null) {
            parameters.add(gameIdFilter);
        }
        if (gameNameFilter != null) {
            parameters.add("%" + gameNameFilter + "%");
        }
        if (userIdFilter != null) {
            parameters.add(userIdFilter);
        }
        if (userNameFilter != null) {
            parameters.add("%" + userNameFilter + "%");
        }
        return parameters.toArray();
    }


}
