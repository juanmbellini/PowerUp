package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.Like;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Models a Review like. Used to track likes and to avoid users from liking reviews multiple times.
 */
@Entity
@Table(name = "review_likes",
        indexes = {@Index(name = "review_likes_pkey",
                columnList = "user_id, review_id", unique = true)})
public class ReviewLike implements ValidationExceptionThrower, Like {

    @Id
    @SequenceGenerator(name = "review_likes_seq", sequenceName = "review_likes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_likes_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "review_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Review review;

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    /*package*/ ReviewLike() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param user   The User liking the review.
     * @param review The liked Review.
     * @throws ValidationException If any value is wrong.
     */
    public ReviewLike(User user, Review review) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(review, errors, ValueErrorConstants.MISSING_REVIEW);
        ValidationHelper.objectNotNull(user, errors, ValueErrorConstants.MISSING_USER);

        throwValidationException(errors);
        this.user = user;
        this.review = review;
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
     * review getter.
     *
     * @return The review.
     */
    public Review getreview() {
        return review;
    }

    /**
     * Created at getter.
     *
     * @return The moment this object is created.
     */
    public Calendar getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewLike)) return false;

        ReviewLike that = (ReviewLike) o;

        return (user == null ? that.user == null : user.equals(that.user))
                && (review == null ? that.review == null : review.equals(that.review));

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (review != null ? review.hashCode() : 0);
        return result;
    }

}
