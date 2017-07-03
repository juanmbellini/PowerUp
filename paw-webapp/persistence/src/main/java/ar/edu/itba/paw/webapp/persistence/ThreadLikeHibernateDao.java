package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ThreadLikeDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    private final UserDao userDao;

    @Autowired
    public ThreadLikeHibernateDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public ThreadLike find(long threadId, long userId) {
        TypedQuery<ThreadLike> baseQuery =
                em.createQuery("FROM ThreadLike AS L where L.thread.id = :threadId AND L.user.id = :userId",
                        ThreadLike.class);
        baseQuery.setParameter("threadId", threadId);
        baseQuery.setParameter("userId", userId);
        try {
            return baseQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean exists(long threadId, long userId) {
        return find(threadId, userId) != null;
    }


    @Override
    public ThreadLike create(Thread thread, User user) {
        if (thread == null || user == null) {
            throw new IllegalArgumentException("Thread and user must not be null");
        }
        ThreadLike threadLike = new ThreadLike(user, thread);
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
