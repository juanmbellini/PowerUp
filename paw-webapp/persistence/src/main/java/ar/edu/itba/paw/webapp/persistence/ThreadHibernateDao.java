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
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
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
        User creator = DaoHelper.findSingleOrThrow(em, User.class, creatorUserId);
        Thread thread = new Thread(creator, title, creatorComment);
        em.persist(thread);
        return thread;
    }


    @Override
    public Set<Thread> findRecent(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY T.createdAt DESC"));
    }

    @Override
    public Set<Thread> findBestPointed(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY SIZE(likes) DESC"));
    }

    @Override
    public Set<Thread> findHottest(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY T.hotValue DESC"));
    }

    @Override
    public Set<Thread> findByUserId(long id) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditions(em, Thread.class, "FROM Thread AS T WHERE T.user.id = ?1", id));
    }

    @Override
    public Set<Thread> findByTitle(String title) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditions(em, Thread.class, "FROM Thread AS T WHERE T.title = ?1", title));
    }

    @Override
    public Thread findById(long threadId) {
        return DaoHelper.findSingle(em, Thread.class, threadId);
    }

    @Override
    public void changeTitle(long threadId, String newTitle) throws NoSuchEntityException, IllegalArgumentException {
        if (newTitle == null) {
            throw new IllegalArgumentException("New thread title can't be null");
        }
        if (newTitle.isEmpty()) {
            throw new IllegalArgumentException("New thread title can't be empty");
        }
        DaoHelper.findSingleOrThrow(em, Thread.class, threadId).setTitle(newTitle);
    }

    @Override
    public void changeInitialCommentTitle(long threadId, String newInitialComment) {
        Thread t = DaoHelper.findSingleOrThrow(em, Thread.class, threadId);
        t.setInitialComment(newInitialComment);
        em.persist(t);
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
    public void editComment(long commentId, String newComment) {
        commentDao.edit(commentId, newComment);
    }

    @Override
    public void deleteComment(long commentId) throws NoSuchEntityException {
        commentDao.delete(commentId);
    }

    @Override
    public void deleteThread(long threadId) throws NoSuchEntityException {
        em.remove(DaoHelper.findSingleOrThrow(em, Thread.class, threadId));
    }

    @Override
    public void updateHotValue(long threadId) {
        Thread thread = DaoHelper.findSingleOrThrow(em, Thread.class, threadId);
        long epoch = LocalDate.parse("2016-01-01").atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        thread.setHotValue((Math.log10(thread.getLikeCount() + 1) + (thread.getUpdatedAt().getTimeInMillis() / 1000 - epoch) / 45000.0));
    }
}
