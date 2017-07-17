package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserFollowDao;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Map<User, Long> countFollowing(Collection<User> users) {
        return countFollow(users, "follower");
    }

    @Override
    public Map<User, Long> countFollowers(Collection<User> users) {
        return countFollow(users, "followed");
    }

    @Override
    public Map<User, Boolean> following(Collection<User> users, User user) {
        return currentFollow(users, "follower", "followed", user);
    }

    @Override
    public Map<User, Boolean> followedBy(Collection<User> users, User user) {
        return currentFollow(users, "followed", "follower", user);
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

    /**
     * Groups {@link User}s with the amount of {@link User} following the or being followed by each of them,
     * according the given {@code fieldName} (i.e "follower" or "followed").
     *
     * @param users     The {@link Collection} of {@link User}s to count its following/followers
     * @param fieldName The field name used to filter (i.e "followed" or "follower")
     * @return A {@link Map} grouping each user with the count.
     */
    private Map<User, Long> countFollow(Collection<User> users, String fieldName) {
        if (users == null) {
            throw new IllegalArgumentException();
        }
        if (users.isEmpty()) {
            return new HashMap<>(); // Avoids querying
        }

        // Used for easily getting a user by its id.
        final Map<Long, User> ids = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        //noinspection unchecked,JpaQlInspection
        final List<Object[]> follows = em.createQuery("SELECT follow." + fieldName + ".id, count(follow)" +
                " FROM UserFollow follow" +
                " WHERE follow." + fieldName + " IN :users" +
                " GROUP BY follow." + fieldName + ".id")
                .setParameter("users", users)
                .getResultList();
        // The follows list holds Object arrays with two elements each: user id and follows count
        final Map<User, Long> result = follows.stream()
                .collect(Collectors.toMap(each -> ids.get((Long) each[0]), each -> (Long) each[1]));
        users.forEach(user -> result.putIfAbsent(user, 0L));
        return result;
    }


    /**
     * Groups {@link User}s together with a flag indicating if the given {@code currentUser}
     * has followed or is being followed by them.
     *
     * @param users       A {@link Collection} of {@link User}s that must be check
     *                    if the given {@code currentUser} is following or is being followed by them.
     * @param currentRole The role the {@code currentUser} has in the relation ("follower" or "followed").
     * @param followRole  The role the {@link User}s in the {@link Collection} has ("follower" or "followed").
     * @param currentUser The current {@link User}.
     * @return A {@link Map} indicating if the {@code currentUser} is following or is being followed by
     * the given {@link Collection} of {@link User}.
     */
    private Map<User, Boolean> currentFollow(Collection<User> users, String currentRole, String followRole,
                                             User currentUser) {
        if (users == null || currentUser == null) {
            throw new IllegalArgumentException();
        }
        if (users.isEmpty()) {
            return new HashMap<>(); // Avoids querying
        }

        //noinspection JpaQlInspection
        final List<User> follow = em.createQuery("SELECT DISTINCT follow." + followRole +
                " FROM UserFollow follow" +
                " WHERE follow." + currentRole + "= :user AND follow." + followRole + " IN :users", User.class)
                .setParameter("user", currentUser)
                .setParameter("users", users)
                .getResultList();

        final Map<User, Boolean> result = follow.stream().collect(Collectors.toMap(Function.identity(), each -> true));
        users.forEach(entity -> result.putIfAbsent(entity, false));
        return result;
    }
}
