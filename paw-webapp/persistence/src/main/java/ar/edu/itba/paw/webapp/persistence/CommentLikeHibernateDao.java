package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CommentLikeDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentLikeHibernateDao implements CommentLikeDao {
    @PersistenceContext
    private EntityManager em;

    private final UserDao userDao;

    @Autowired
    public CommentLikeHibernateDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public CommentLike find(long commentId, long userId) {
        TypedQuery<CommentLike> baseQuery = em.createQuery("FROM CommentLike AS L " +
                        "where L.comment.id = :commentId AND L.user.id = :userId",
                CommentLike.class);
        baseQuery.setParameter("commentId", commentId);
        baseQuery.setParameter("userId", userId);
        List<CommentLike> result = baseQuery.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public CommentLike create(Comment comment, User user) {
        if (comment == null || user == null) {
            throw new IllegalArgumentException("Comment and/or user must not be null");
        }
        CommentLike commentLike = new CommentLike(user, comment);
        em.persist(commentLike);
        return commentLike;
    }

    public void delete(CommentLike commentLike) {
        if (commentLike == null) {
            throw new IllegalArgumentException("The commentLike must not be null");
        }
        em.remove(commentLike);
    }
}
