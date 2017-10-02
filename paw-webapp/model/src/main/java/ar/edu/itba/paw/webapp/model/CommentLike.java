package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.Like;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Models a {@link Comment} like. Used to track likes and to avoid users from liking comments multiple times.
 */
@Entity
@Table(name = "comment_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "comment_id"}))
public class CommentLike implements ValidationExceptionThrower, Like {


    @Id
    @SequenceGenerator(name = "comment_likes_seq", sequenceName = "comment_likes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_likes_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Comment comment;

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    /*package*/ CommentLike() {
        // For Hibernate
    }


    /**
     * Constructor.
     *
     * @param user    The {@link User} liking the comment.
     * @param comment The liked {@link Comment}.
     * @throws ValidationException If any value is wrong.
     */
    public CommentLike(User user, Comment comment) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(comment, errors, ValueErrorConstants.MISSING_COMMENT);
        ValidationHelper.objectNotNull(user, errors, ValueErrorConstants.MISSING_USER);
        throwValidationException(errors);

        this.user = user;
        this.comment = comment;
    }

    /**
     * Id getter.
     *
     * @return The id.
     */
    public long getId() {
        return id;
    }

    /**
     * User getter.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Comment getter.
     *
     * @return The comment.
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Created at getter.
     *
     * @return The moment this object is created.
     */
    public Calendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Equals based on the id.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the same, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentLike comment = (CommentLike) o;

        return id == comment.id;
    }

    /**
     * Hashcode based on the id.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
