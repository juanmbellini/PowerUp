package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CommentHibernateDao implements CommentDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Comment comment(Thread thread, User commenter, String commentMessage) {
        final Comment comment = new Comment(thread, commenter, commentMessage);
        em.persist(comment);
        return comment;
    }

    @Override
    public Comment reply(Comment comment, User commenter, String commentMessage) {
        final Comment reply = new Comment(comment, commenter, commentMessage);
        em.persist(comment);
        return comment;
    }

    @Override
    public void update(Comment comment, String newComment) {
        if (comment == null) {
            throw new IllegalArgumentException("The comment must not be null.");
        }
        comment.update(comment.getThread(), comment.getParentComment(), comment.getCommenter(), newComment);
        em.merge(comment);
    }

    @Override
    public void delete(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("The comment must not be null.");
        }
        em.remove(comment);
    }


    @Override
    public Comment findById(long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public Comment findComment(long threadId, long userId) {
        try {
            return em.createQuery("FROM Comment AS C where C.thread.id = :threadId AND L.user.id = :userId",
                    Comment.class)
                    .setParameter("threadId", threadId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Comment findReply(long commentId, long userId) {
        try {
            return em.createQuery("FROM Comment AS C where C.parentComment.id = :commentId AND L.user.id = :userId",
                    Comment.class)
                    .setParameter("commentId", commentId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
