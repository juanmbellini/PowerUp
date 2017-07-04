package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ThreadLikeDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        if (threads == null) {
            throw new IllegalArgumentException();
        }
        if (threads.isEmpty()) {
            return new HashMap<>(); // Avoids querying
        }

        // Used for easily getting a Thread by its id.
        final Map<Long, Thread> ids = threads.stream().collect(Collectors.toMap(Thread::getId, Function.identity()));
        //noinspection unchecked, JpaQlInspection
        final List<Object[]> likes = em.createQuery("SELECT _like.thread.id, count(_like)" +
                " FROM ThreadLike _like" +
                " WHERE _like.thread IN :threads" +
                " GROUP BY _like.thread.id")
                .setParameter("threads", threads)
                .getResultList();
        // The likes list holds Object arrays with two elements each: thread id and likes count
        final Map<Thread, Long> result = likes.stream().collect(Collectors.toMap(each -> ids.get((Long) each[0]), each -> (Long) each[1]));
        threads.forEach(thread -> result.putIfAbsent(thread, 0L));
        return result;
    }
}
