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
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

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
    public Set<Thread> findBestPointed(int limit) {
        TypedQuery<Thread> baseQuery = em.createQuery("FROM Thread AS T ORDER BY size(likes) DESC", Thread.class);
        baseQuery.setMaxResults(limit);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Thread> findHottest(int limit) {
        TypedQuery<Thread> baseQuery = em.createQuery("FROM Thread AS T ORDER BY T.hotValue DESC", Thread.class);
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
    public void changeInitialCommentTitle(long threadId, String newInitialComment) {
        Thread t = DaoHelper.findSingleOrThrow(em, Thread.class, threadId);
        t.setInitialComment(newInitialComment);
        em.persist(t);
    }

    @Override
    public int likeThread(long threadId, long userId) {
        int likeInt = threadLikeDao.create(threadId, userId);
        em.flush();
        updateHotValue(findById(threadId));
        return likeInt;
    }

    @Override
    public int unlikeThread(long threadId, long userId) {
        int likeInt = threadLikeDao.delete(threadId, userId);
        em.flush();
        updateHotValue(findById(threadId));
        return likeInt;
    }

    @Override
    public Comment comment(long threadId, long commenterId, String comment) {

        Comment commentToReturn =commentDao.comment(threadId, commenterId, comment);
        em.flush(); updateHotValue(findById(threadId));
        return commentToReturn;
    }

    @Override
    public Comment replyToComment(long commentId, long replierId, String reply) {
        Comment commentToReturn =commentDao.reply(commentId, replierId, reply);
        em.flush();
        updateHotValue(commentToReturn.getThread());
        return commentToReturn;
    }

    private void updateHotValue(Thread thread) {
        LocalDate ldt = LocalDate.parse("2016-01-01");
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch = ldt.atStartOfDay(zoneId).toEpochSecond();
        thread.setHotValue((Math.log10(thread.getLikeCount()+1)+(thread.getUpdatedAt().getTimeInMillis()/1000 - epoch)/45000.0));
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
    public void editComment(long commentId, String newComment) {
        commentDao.edit(commentId, newComment);
        em.flush(); updateHotValue(commentDao.findById(commentId).getThread());
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
