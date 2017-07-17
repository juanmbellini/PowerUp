package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ReviewLikeDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.ReviewLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Map;

@Repository
public class ReviewLikeHibernateDao implements ReviewLikeDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Holds the base query for searching and checking existence of {@link ReviewLike}.
     */
    private static final String BASE_QUERY = "FROM ReviewLike _like" +
            " WHERE _like.review = ?1 AND _like.user = ?2";

    @Override
    public Page<ReviewLike> getLikes(Review review, int pageNumber, int pageSize,
                                     SortingType sortingType, SortDirection sortDirection) {

        return DaoHelper.getLikesPage(em, pageNumber, pageSize, sortingType.getFieldName(), sortDirection,
                ReviewLike.class,
                new DaoHelper.ConditionAndParameterWrapper("_like.review = ?0", review, 0));
    }

    @Override
    public ReviewLike find(Review review, User user) {
        return DaoHelper.findSingleWithConditions(em, ReviewLike.class, "SELECT _like " + BASE_QUERY, review, user);
    }

    @Override
    public boolean exists(Review review, User user) {
        return DaoHelper.exists(em, "SELECT COUNT(_like) " + BASE_QUERY, review, user);
    }


    @Override
    public ReviewLike create(Review review, User user) {
        if (review == null || user == null) {
            throw new IllegalArgumentException("review and user must not be null");
        }
        final ReviewLike reviewLike = new ReviewLike(user, review);
        em.persist(reviewLike);
        return reviewLike;
    }

    @Override
    public void delete(ReviewLike reviewLike) {
        if (reviewLike == null) {
            throw new IllegalArgumentException("The ReviewLike must not be null");
        }
        em.remove(reviewLike);
    }

    @Override
    public Map<Review, Long> countLikes(Collection<Review> reviews) {
        return DaoHelper.countLikes(reviews, em, "review", ReviewLike.class);
    }


    @Override
    public Map<Review, Boolean> likedBy(Collection<Review> reviews, User user) {
        return DaoHelper.likedBy(reviews, user, em, Review.class);
    }
}
