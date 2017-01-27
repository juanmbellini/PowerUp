package ar.edu.itba.paw.webapp.model;

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
public class CommentLike {

    private static final ValidationException.ValueError MISSING_COMMENT_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "comment", "A comment must be set.");
    private static final ValidationException.ValueError MISSING_USER_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "user", "An user must be set.");

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
        //For Hibernate
    }


    public CommentLike(User user, Comment comment) {
        List<ValidationException.ValueError> errors = new LinkedList<>();
        if (comment == null) {
            errors.add(MISSING_COMMENT_ERROR);
        }
        if (user == null) {
            errors.add(MISSING_USER_ERROR);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        this.user = user;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Comment getComment() {
        return comment;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentLike comment = (CommentLike) o;

        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
