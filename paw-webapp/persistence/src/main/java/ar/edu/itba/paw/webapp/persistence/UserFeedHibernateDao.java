package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NumberOfPageBiggerThanTotalAmountException;
import ar.edu.itba.paw.webapp.interfaces.UserFeedDao;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserGameStatus;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Juan Marcos Bellini on 17/7/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Repository
public class UserFeedHibernateDao implements UserFeedDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Thread> getThreads(User user, int pageNumber, int pageSize) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        // Count and filtering
        final String subQuery = subQuery(user, "threads", "user_id");
        final long count = countList(subQuery, user, em);
        if (count == 0) {
            return Page.emptyPage();
        }

        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / pageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }

        //noinspection StringBufferReplaceableByString
        final String query = new StringBuilder()
                .append("SELECT * FROM threads WHERE id IN (").append(subQuery).append(")").toString();

        // Data extraction, paging and sorting
        final List<Thread> threads = getList(query, pageNumber, pageSize, user, em, Thread.class);


        return DaoHelper.createPage(threads, pageSize, pageNumber, totalAmountOfPages, count);
    }

    @Override
    public Page<Review> getReviews(User user, int pageNumber, int pageSize) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        // Count and filtering
        final String subQuery = subQuery(user, "reviews", "user_id");
        final long count = countList(subQuery, user, em);
        if (count == 0) {
            return Page.emptyPage();
        }

        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / pageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }

        //noinspection StringBufferReplaceableByString
        final String query = new StringBuilder()
                .append("SELECT * FROM reviews WHERE id IN (").append(subQuery).append(")").toString();

        // Data extraction, paging and sorting
        final List<Review> reviews = getList(query, pageNumber, pageSize, user, em, Review.class);

        return DaoHelper.createPage(reviews, pageSize, pageNumber, totalAmountOfPages, count);
    }

    @Override
    public Page<UserGameStatus> getPlayStatuses(User user, int pageNumber, int pageSize) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        // Count and filtering
        final String subQuery = subQuery(user, "game_play_statuses", "user_id");
        final long count = countList(subQuery, user, em);
        if (count == 0) {
            return Page.emptyPage();
        }

        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / pageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }

        //noinspection StringBufferReplaceableByString
        final String query = new StringBuilder()
                .append("SELECT * FROM game_play_statuses WHERE id IN (").append(subQuery).append(")").toString();

        // Data extraction, paging and sorting
        final List<UserGameStatus> statuses = getList(query, pageNumber, pageSize, user, em, UserGameStatus.class);

        return DaoHelper.createPage(statuses, pageSize, pageNumber, totalAmountOfPages, count);
    }

    private static <T> String subQuery(User user, String tableName, String tableUserFieldName) {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder()
                .append("SELECT DISTINCT ").append(tableName).append(".id")
                .append(" FROM ").append(tableName).append(" other")
                .append(" INNER JOIN user_follow uf ON uf.followed_id = other.").append(tableUserFieldName)
                .append(" WHERE follower_id = :userId").toString();
    }

    /**
     * Counts the amount of entities does the given {@code subQuery} returns.
     *
     * @param subQuery The {@link String} holding the SQL query.
     * @param user     The {@link User} being used in the given {@code subQuery}
     * @param em       The {@link EntityManager} used to perform the query.
     * @return The amount of entities.
     */
    private static long countList(String subQuery, User user, EntityManager em) {

        return ((BigInteger) em.createNativeQuery("SELECT COUNT(*) FROM (" + subQuery + ") AS c")
                .setParameter("userId", user.getId()).getSingleResult()).longValue();
    }


    /**
     * Returns the {@link List} of entities of the speficied type {@code T} according to the given query.
     *
     * @param query      The {@link String} holding the SQL query.
     * @param pageNumber The {@link Page} number.
     * @param pageSize   The {@link Page} size.
     * @param user       The {@link User} owning the list.
     * @param em         The {@link EntityManager} used to perform the query.
     * @param klass      The {@link Class} of the entities being returned.
     * @param <T>        The actual type of the entities.
     * @return The {@link List} of entities.
     */
    private static <T> List<T> getList(String query, int pageNumber, int pageSize, User user,
                                       EntityManager em, Class<T> klass) {
        // Data extraction, paging and sorting query
        final Query dataQuery = em.createNativeQuery(query, klass)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("userId", user.getId());
        //noinspection unchecked
        return dataQuery.getResultList();
    }
}
