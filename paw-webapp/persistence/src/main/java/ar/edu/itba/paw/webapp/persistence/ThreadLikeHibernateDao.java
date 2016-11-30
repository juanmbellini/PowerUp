package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
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
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean exists(long threadId, long userId) {
        return find(threadId, userId) != null;
    }

    @Override
    public int create(long threadId, long userId) {
        User user = userDao.findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        Thread thread = em.find(Thread.class, threadId);
        if(thread == null) {
            throw new NoSuchEntityException(Thread.class, threadId);
        }
        if(!exists(threadId, userId)) {
            //TODO is this check necessary? What happens with the @Table annotation?
//            LoggerFactory.logger(this.getClass()).errorf("{} couldn't like thread {}: {}", user.getUsername(), thread.getId(), e);
            em.persist(new ThreadLike(user, thread));
        }
        return thread.getLikeCount();
    }

    @Override
    public int delete(long threadId, long userId) {
        int result = -1;
        ThreadLike like = find(threadId, userId);
        if(like != null) {
            result = like.getThread().getLikeCount() - 1;
            em.remove(like);
        }
        return result;
    }
}
