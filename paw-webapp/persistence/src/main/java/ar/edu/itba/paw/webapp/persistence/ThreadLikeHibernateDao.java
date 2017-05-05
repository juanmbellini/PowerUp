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
        TypedQuery<ThreadLike> baseQuery = em.createQuery("FROM ThreadLike AS L where L.thread.id = :threadId AND L.user.id = :userId", ThreadLike.class);
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
}
