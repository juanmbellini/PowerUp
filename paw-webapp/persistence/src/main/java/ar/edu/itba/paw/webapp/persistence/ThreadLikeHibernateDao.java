package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.ThreadLikeDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Map;

@Repository
public class ThreadLikeHibernateDao implements ThreadLikeDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Holds the base query for searching and checking existence of {@link ThreadLike}.
     */
    private static final String BASE_QUERY = "FROM ThreadLike _like" +
            " WHERE _like.thread = ?1 AND _like.user = ?2";

    @Override
    public Page<ThreadLike> getLikes(Thread thread, int pageNumber, int pageSize,
                                     SortingType sortingType, SortDirection sortDirection) {

        return DaoHelper.getLikesPage(em, pageNumber, pageSize, sortingType.getFieldName(), sortDirection,
                ThreadLike.class,
                new DaoHelper.ConditionAndParameterWrapper("_like.thread = ?0", thread, 0));
    }

    @Override
    public ThreadLike find(Thread thread, User user) {
        return DaoHelper.findSingleWithConditions(em, ThreadLike.class, "SELECT _like " + BASE_QUERY, thread, user);
    }

    @Override
    public boolean exists(Thread thread, User user) {
        return DaoHelper.exists(em, "SELECT COUNT(_like) " + BASE_QUERY, thread, user);
    }


    @Override
    public ThreadLike create(Thread thread, User user) {
        if (thread == null || user == null) {
            throw new IllegalArgumentException("Thread and user must not be null");
        }
        final ThreadLike threadLike = new ThreadLike(user, thread);
        em.persist(threadLike);
        return threadLike;
    }

    @Override
    public void delete(ThreadLike threadLike) {
        if (threadLike == null) {
            throw new IllegalArgumentException("The threadLike must not be null");
        }
        em.remove(threadLike);
    }

    @Override
    public Map<Thread, Long> countLikes(Collection<Thread> threads) {
        return DaoHelper.countLikes(threads, em, "thread", ThreadLike.class);
    }


    @Override
    public Map<Thread, Boolean> likedBy(Collection<Thread> threads, User user) {
        return DaoHelper.likedBy(threads, user, em, Thread.class);
    }
}
