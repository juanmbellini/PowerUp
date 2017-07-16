package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserFollowDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by julian on 16/07/17.
 */

@Repository
public class UserFollowHibernateDao implements UserFollowDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Holds the base query for searching and checking existence of UserFollow.
     */
    private static final String BASE_QUERY = "FROM UserFollow follow" +
            " WHERE _follow.follower = ?1 AND follow.followed = ?2";

    @Override
    public Page<UserFollow> getUserFollowing(User user, int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {
        return DaoHelper.getFollowingPage(em, pageNumber, pageSize, sortingType.getFieldName(), sortDirection,
                UserFollow.class,
                new DaoHelper.ConditionAndParameterWrapper("follow.follower = ?0", user, 0));
    }

    @Override
    public Page<UserFollow> getUserFollowedBy(User user, int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {
        return DaoHelper.getFollowedByPage(em, pageNumber, pageSize, sortingType.getFieldName(), sortDirection,
                UserFollow.class,
                new DaoHelper.ConditionAndParameterWrapper("follow.followed = ?0", user, 0));
    }

    @Override
    public UserFollow find(User follower, User followed) {
        return DaoHelper.findSingleWithConditions(em, UserFollow.class, "SELECT follow " + BASE_QUERY, follower, followed);
    }

    @Override
    public boolean exists(User follower, User followed) {
        return DaoHelper.exists(em, "SELECT follow " + BASE_QUERY, follower, followed);
    }


    @Override
    public UserFollow create(User follower, User followed) {
        if (follower == null || followed == null) {
            throw new IllegalArgumentException("Follower and followed must not be null");
        }
        final UserFollow userFollow = new UserFollow(follower, followed);
        em.persist(userFollow);
        return userFollow;
    }

    @Override
    public void delete(UserFollow userFollow) {
        if (userFollow == null) {
            throw new IllegalArgumentException("The userFollow must not be null");
        }
        em.remove(userFollow);
    }
}
