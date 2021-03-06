package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by juanlipuma on Nov/7/16.
 */
@Repository
public class ReviewHibernateDao implements ReviewDao {


    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String usernameFilter,
                                   int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {
        // First we sanitize the string values.
        gameNameFilter = Optional.ofNullable(gameNameFilter).map(DaoHelper::escapeUnsafeCharacters).orElse(null);
        usernameFilter = Optional.ofNullable(usernameFilter).map(DaoHelper::escapeUnsafeCharacters).orElse(null);


        final StringBuilder query = new StringBuilder()
                .append("FROM Review review");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        int conditionNumber = 0;
        if (gameIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("game.id = ?" + conditionNumber,
                    gameIdFilter, conditionNumber));
            conditionNumber++;
        }
        if (gameNameFilter != null && !gameNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(game.name) LIKE ?" + conditionNumber,
                    "%" + gameNameFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (userIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.id = ?" + conditionNumber,
                    userIdFilter, conditionNumber));
            conditionNumber++;
        }
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(user.username) LIKE ?" + conditionNumber,
                    "%" + usernameFilter.toLowerCase() + "%", conditionNumber));
        }

        return DaoHelper.findPageWithConditions(em, Review.class, query, "review", "review.id", conditions,
                pageNumber, pageSize, sortingType.getFieldName(), sortDirection, false);

    }

    @Override
    public Review findById(long reviewId) {
        return em.find(Review.class, reviewId);
    }

    @Override
    public Review create(User reviewer, Game game, String reviewBody) {
        final Review review =
                new Review(reviewer, game, reviewBody);
        em.persist(review);
        return review;
    }

    @Override
    public void changeReviewBody(Review review, String newBody) {
        if (review == null) {
            throw new IllegalArgumentException("The review can not be null.");
        }
        review.changeBody(newBody);
        em.merge(review);
    }

    @Override
    public void delete(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("The review can not be null.");
        }
        em.remove(review);
    }
}
