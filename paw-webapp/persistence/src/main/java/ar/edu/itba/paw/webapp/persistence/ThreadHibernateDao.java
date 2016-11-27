package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadLikeDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class ThreadHibernateDao implements ThreadDao {
    @PersistenceContext
    private EntityManager em;

    private final ThreadLikeDao threadLikeDao;

    private final CommentDao commentDao;

    @Autowired
    public ThreadHibernateDao(ThreadLikeDao threadLikeDao, CommentDao commentDao) {
        this.threadLikeDao = threadLikeDao;
        this.commentDao = commentDao;
    }

    @Override
    public Thread create(String title, long creatorUserId, String creatorComment) throws NoSuchEntityException {
        User creator = find(User.class, creatorUserId);
        Thread thread = new Thread(creator, title, creatorComment);
        em.persist(thread);
        return thread;
    }

    @Override
    public Set<Thread> findRecent(int limit) {
        TypedQuery<Thread> baseQuery = em.createQuery("FROM Thread AS T ORDER BY T.updatedAt DESC", Thread.class);
        baseQuery.setMaxResults(limit);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Thread> findByUserId(long id) {
        TypedQuery<Thread> baseQuery = em.createQuery("FROM Thread AS T where T.user.id = :id", Thread.class);
        baseQuery.setParameter("id", id);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Thread> findByTitle(String title) {
        TypedQuery<Thread> baseQuery = em.createQuery("FROM Thread AS T where T.title = :title", Thread.class);
        baseQuery.setParameter("title", title);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Thread findById(long threadId) {
        return em.find(Thread.class, threadId);
    }

    @Override
    public void changeTitle(long threadId, String newTitle) throws NoSuchEntityException, IllegalArgumentException {
        if(newTitle == null) {
            throw new IllegalArgumentException("New thread title can't be null");
        }
        if(newTitle.isEmpty()) {
            throw new IllegalArgumentException("New thread title can't be empty");
        }
        Thread t = find(Thread.class, threadId);
        t.setTitle(newTitle);
    }

    @Override
    public int likeThread(long threadId, long userId) {
        return threadLikeDao.create(threadId, userId);
    }

    @Override
    public int unlikeThread(long threadId, long userId) {
        return threadLikeDao.delete(threadId, userId);
    }

    @Override
    public Comment comment(long threadId, long commenterId, String comment) {
        return commentDao.comment(threadId, commenterId, comment);
    }

    @Override
    public Comment replyToComment(long commentId, long replierId, String reply) {
        return commentDao.reply(commentId, replierId, reply);
    }

    @Override
    public int likeComment(long commentId, long userId) {
        return commentDao.like(commentId, userId);
    }

    @Override
    public int unlikeComment(long commentId, long userId) {
        return commentDao.unlike(commentId, userId);
    }

    @Override
    public void deleteComment(long commentId) throws NoSuchEntityException {
        commentDao.delete(commentId);
    }

    @Override
    public void deleteThread(long threadId) throws NoSuchEntityException {
        em.remove(find(Thread.class, threadId));
    }

    /**
     * Attempts to find an entity. If found, returns it, otherwise throws exception.
     *
     * @param klass The class of the entity to find
     * @param id The ID of the entity to find.
     * @return The found entity.
     * @throws NoSuchEntityException If no such entity find.
     */
    private <T> T find(Class<T> klass, long id) {
        T result = em.find(klass, id);
        if (result == null) {
            throw new NoSuchEntityException(klass, id);
        }
        return result;
    }
}
