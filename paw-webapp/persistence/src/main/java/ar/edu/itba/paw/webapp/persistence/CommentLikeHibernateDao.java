package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.CommentLikeDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class CommentLikeHibernateDao implements CommentLikeDao {
    @PersistenceContext
    private EntityManager em;

    private final UserDao userDao;

    private final CommentDao commentDao;

    @Autowired
    public CommentLikeHibernateDao(UserDao userDao, CommentDao commentDao) {
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    @Override
    public CommentLike find(long commentId, long userId) {
        TypedQuery<CommentLike> baseQuery = em.createQuery("FROM CommentLike AS L where L.comment.id = :commentId AND L.user.id = :userId", CommentLike.class);
        baseQuery.setParameter("commentId", commentId);
        baseQuery.setParameter("userId", userId);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    @Override
    public int create(long commentId, long userId) {
        User user = userDao.findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        Comment comment = commentDao.findById(commentId);
        if(comment == null) {
            throw new NoSuchEntityException(Comment.class, commentId);
        }
        if(find(commentId, userId) == null) {
            //TODO is this check necessary? What happens with the @Table annotation?
//            LoggerFactory.logger(this.getClass()).errorf("{} couldn't like comment {}: {}", user.getUsername(), comment.getId(), e);
            em.persist(new CommentLike(user, comment));
        }
        return comment.getLikeCount();
    }

    @Override
    public int delete(long commentId, long userId) {
        int result = -1;
        CommentLike like = find(commentId, userId);
        if(like != null) {
            result = like.getComment().getLikeCount() - 1;
            em.remove(like);
        }
        return result;
    }
}
