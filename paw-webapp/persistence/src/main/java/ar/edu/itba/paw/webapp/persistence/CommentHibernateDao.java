package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CommentHibernateDao implements CommentDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Comment> getThreadComments(Thread thread, int pageNumber, int pageSize,
                                           SortingType sortingType, SortDirection sortDirection) {

        final StringBuilder query = new StringBuilder()
                .append("FROM Comment comment");


        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        conditions.add(new DaoHelper.ConditionAndParameterWrapper("thread = ?0 AND parentComment is null", thread, 0));

        return DaoHelper.findPageWithConditions(em, Comment.class, query, "comment", "comment.id", conditions,
                pageNumber, pageSize, sortingType.getFieldName(), sortDirection, false);
    }

    @Override
    public Comment comment(Thread thread, String body, User commenter) {
        final Comment comment = new Comment(thread, body, commenter);
        em.persist(comment);
        return comment;
    }

    @Override
    public Comment reply(Comment comment, String body, User replier) {
        final Comment reply = new Comment(comment, body, replier);
        em.persist(comment);
        return comment;
    }

    @Override
    public void update(Comment comment, String newComment) {
        if (comment == null) {
            throw new IllegalArgumentException("The comment must not be null.");
        }
        comment.update(newComment);
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
        return DaoHelper.findSingleWithConditions(em, Comment.class,
                "FROM Comment AS c where c.parentComment IS NULL AND c.thread.id = ?1 AND c.commenter.id = ?2",
                threadId, userId);
    }

    @Override
    public Comment findReply(long commentId, long userId) {
        return DaoHelper.findSingleWithConditions(em, Comment.class,
                "FROM Comment AS c where c.parentComment.id = ?1 AND c.commenter.id = ?2",
                commentId, userId);
    }


}
