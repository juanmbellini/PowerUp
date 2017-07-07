package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CommentLikeDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Map;

@Repository
public class CommentLikeHibernateDao implements CommentLikeDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Holds the base query for searching and checking existence of {@link CommentLike}.
     */
    private static final String BASE_QUERY = "FROM CommentLike _like" +
            " WHERE _like.comment = ?1 AND _like.user = ?2";


    @Override
    public CommentLike find(Comment comment, User user) {
        return DaoHelper.findSingleWithConditions(em, CommentLike.class, "SELECT _like " + BASE_QUERY, comment, user);
    }

    @Override
    public boolean exists(Comment comment, User user) {
        return DaoHelper.exists(em, "SELECT COUNT(_like) " + BASE_QUERY, comment, user);
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

    @Override
    public Map<Comment, Long> countLikes(Collection<Comment> comments) {
        return DaoHelper.countLikes(comments, em, "comment", CommentLike.class);
    }


}
