package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserFollowDao;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

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
            " WHERE follow.follower = ?1 AND follow.followed = ?2";

    @Override
    public Page<UserFollow> getFollowing(User user, int pageNumber, int pageSize, SortDirection sortDirection) {
        return getFollowingPage(pageNumber, pageSize, sortDirection, "follower", user);
    }

    @Override
    public Page<UserFollow> getFollowers(User user, int pageNumber, int pageSize, SortDirection sortDirection) {
        return getFollowingPage(pageNumber, pageSize, sortDirection, "followed", user);
    }

    @Override
    public UserFollow find(User follower, User followed) {
        return DaoHelper.findSingleWithConditions(em, UserFollow.class, "SELECT follow " + BASE_QUERY,
                follower, followed);
    }

    @Override
    public boolean exists(User follower, User followed) {
        return DaoHelper.exists(em, "SELECT COUNT(follow) " + BASE_QUERY, follower, followed);
    }


    @Override
    public UserFollow create(User followed, User follower) {
        if (follower == null || followed == null) {
            throw new IllegalArgumentException("Follower and followed must not be null");
        }
        final UserFollow userFollow = new UserFollow(followed, follower);
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


    /**
     * Creates a {@link Page} of {@link UserFollow} according to the given {@code fieldName},
     * which says if followers of following {@link User}s must be returned.
     *
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortDirection The sort direction.
     * @param fieldName     The field that says if followers or following {@link User}s must be returned.
     * @param user          The {@link User} whose information must be retrieved.
     * @return The resulting page.
     */
    private Page<UserFollow> getFollowingPage(int pageNumber, int pageSize, SortDirection sortDirection,
                                              String fieldName, User user) {
        final StringBuilder query = new StringBuilder()
                .append("FROM ").append(UserFollow.class.getSimpleName()).append(" follow");
        return DaoHelper.findPageWithConditions(em, UserFollow.class, query, "follow", "follow.id",
                Collections.singletonList(new DaoHelper
                        .ConditionAndParameterWrapper("follow." + fieldName + " = ?0", user, 0)),
                pageNumber, pageSize, "follow.id", sortDirection, false);
    }
}
