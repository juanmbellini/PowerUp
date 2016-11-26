package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.CommentLikeDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class CommentHibernateDao implements CommentDao {
    @PersistenceContext
    private EntityManager em;

    private final CommentLikeDao commentLikeDao;

    private final UserDao userDao;

    private final ThreadDao threadDao;

    @Autowired
    public CommentHibernateDao(CommentLikeDao commentLikeDao, UserDao userDao, ThreadDao threadDao) {
        this.commentLikeDao = commentLikeDao;
        this.userDao = userDao;
        this.threadDao = threadDao;
    }

    @Override
    public void comment(long threadId, long commenterId, String comment) {
        Thread thread = threadDao.findById(threadId);
        if(thread == null) {
            throw new NoSuchEntityException(Thread.class, threadId);
        }
        User commenter = userDao.findById(commenterId);
        if(commenter == null) {
            throw new NoSuchUserException(commenterId);
        }
        Comment c = new Comment(thread, commenter, comment);
    }

    @Override
    public void reply(long commentId, long replierId, String reply) {
        Comment parent = findById(commentId);
        if(parent == null) {
            throw new NoSuchEntityException(Comment.class, commentId);
        }
        User replier = userDao.findById(replierId);
        if(replier == null) {
            throw new NoSuchUserException(replierId);
        }
        Comment c = new Comment(parent, replier, reply);
    }

    @Override
    public Comment findById(long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public Comment findComment(long threadId, long userId) {
        TypedQuery<Comment> baseQuery = em.createQuery("FROM Comment AS C where C.thread.id = :threadId AND L.user.id = :userId", Comment.class);
        baseQuery.setParameter("threadId", threadId);
        baseQuery.setParameter("userId", userId);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Comment findReply(long commentId, long userId) {
        //TODO order by?
        TypedQuery<Comment> baseQuery = em.createQuery("FROM Comment AS C where C.parentComment.id = :commentId AND L.user.id = :userId", Comment.class);
        baseQuery.setParameter("commentId", commentId);
        baseQuery.setParameter("userId", userId);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public int like(long id, long userId) {
        return commentLikeDao.create(id, userId);
    }

    @Override
    public int unlike(long id, long userId) {
        return commentLikeDao.delete(id, userId);
    }

    @Override
    public void delete(long id) {
        Comment c = findById(id);
        if(c != null) {
            em.remove(c);
        }
    }
}
