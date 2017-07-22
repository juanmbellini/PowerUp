package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.model_interfaces.Commentable;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Comment findById(long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public Comment comment(Thread thread, String body, User commenter) {
        final Comment comment = new Comment(thread, body, commenter);
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
    public Page<Comment> getCommentReplies(Comment comment, int pageNumber, int pageSize,
                                           SortingType sortingType, SortDirection sortDirection) {
        final StringBuilder query = new StringBuilder()
                .append("FROM Comment comment");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        conditions.add(new DaoHelper.ConditionAndParameterWrapper("parentComment = ?0", comment, 0));

        return DaoHelper.findPageWithConditions(em, Comment.class, query, "comment", "comment.id", conditions,
                pageNumber, pageSize, sortingType.getFieldName(), sortDirection, false);
    }

    @Override
    public Comment reply(Comment comment, String body, User replier) {
        final Comment reply = new Comment(comment, body, replier);
        em.persist(reply);
        return reply;
    }


    @Override
    public Map<Thread, Long> countComments(Collection<Thread> threads) {
        if (threads == null) {
            throw new IllegalArgumentException();
        }
        if (threads.isEmpty()) {
            return new HashMap<>(); // Avoids querying
        }

        // Used for easily getting an entity by its id.
        final Map<Long, Thread> ids = threads.stream()
                .collect(Collectors.toMap(Commentable::getId, Function.identity()));
        //noinspection unchecked, JpaQlInspection
        final List<Object[]> comments = em.createQuery("SELECT comment.thread.id, count(comment)" +
                " FROM Comment comment" +
                " WHERE comment.parentComment is null AND comment.thread IN :threads" +
                " GROUP BY comment.thread.id")
                .setParameter("threads", threads)
                .getResultList();
        // The comments list holds Object arrays with two elements each: comment id and likes count
        final Map<Thread, Long> result = comments.stream()
                .collect(Collectors.toMap(each -> ids.get((Long) each[0]), each -> (Long) each[1]));
        threads.forEach(thread -> result.putIfAbsent(thread, 0L));
        return result;
    }


    @Override
    public Map<Comment, Long> countReplies(Collection<Comment> comments) {
        if (comments == null) {
            throw new IllegalArgumentException();
        }
        if (comments.isEmpty()) {
            return new HashMap<>(); // Avoids querying
        }

        // Used for easily getting an entity by its id.
        final Map<Long, Comment> ids = comments.stream()
                .collect(Collectors.toMap(Commentable::getId, Function.identity()));
        //noinspection unchecked, JpaQlInspection
        final List<Object[]> likes = em.createQuery("SELECT comment.parentComment.id, count(comment)" +
                " FROM Comment comment" +
                " WHERE comment.parentComment IN :comments" +
                " GROUP BY comment.parentComment.id")
                .setParameter("comments", comments)
                .getResultList();
        // The likes list holds Object arrays with two elements each: entity id and likes count
        final Map<Comment, Long> result = likes.stream()
                .collect(Collectors.toMap(each -> ids.get((Long) each[0]), each -> (Long) each[1]));
        comments.forEach(comment -> result.putIfAbsent(comment, 0L));
        return result;
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
