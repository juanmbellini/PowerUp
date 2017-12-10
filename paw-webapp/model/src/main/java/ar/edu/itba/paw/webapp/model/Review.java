package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;

/**
 * Models a review made by a specific user for a specific game.
 */
@Entity
@Table(name = "reviews")
public class Review implements ValidationExceptionThrower, Likeable {

    @Id
    @SequenceGenerator(name = "reviews_seq", sequenceName = "reviews_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Calendar date;

    @Column(nullable = false)
    private String review;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<ReviewLike> likes;

    /*package*/  Review() {
        this.likes = new HashSet<>();
        // For Hibernate
    }


    /**
     * Creates a review.
     *
     * @param user       The user creating the review.
     * @param game       The reviewed game.
     * @param reviewBody THe body of the review.
     * @throws ValidationException If any value is wrong.
     */
    public Review(User user, Game game, String reviewBody)
            throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(user, errorList, ValueErrorConstants.MISSING_USER);
        ValidationHelper.objectNotNull(game, errorList, ValueErrorConstants.MISSING_GAME);
        validateReviewBody(reviewBody, errorList);
        throwValidationException(errorList); // Throws exception in case the list is not empty.

        this.user = user;
        this.game = game;
        this.date = Calendar.getInstance();
        this.review = reviewBody;
    }

    /**
     * Changes the body to this review.
     *
     * @param newReviewBody The new body for this review.
     * @throws ValidationException In case the body is not valid.
     */
    public void changeBody(String newReviewBody) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        validateReviewBody(newReviewBody, errorList);
        throwValidationException(errorList); // Throws exception in case the list is not empty.

        this.review = newReviewBody;
    }

    /**
     * Id getter.
     *
     * @return The id.
     */
    public long getId() {
        return id;
    }

    @Override
    public long getLikeCount() {
        return likes.size();
    }

    /**
     * Game getter.
     *
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Review getter.
     *
     * @return The review (i.e The body of the review).
     */
    public String getReview() {
        return review;
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
     * Date getter.
     *
     * @return The date (i.e. Date at which the review was created).
     */
    public Calendar getDate() {
        return date;
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

        Review review = (Review) o;

        return id == review.id;

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

    /**
     * Validates the given {@code reviewBody}, adding the corresponding {@link ValueError} to the given {@link List}
     * in case the body is not valid.
     *
     * @param reviewBody The review body to be validated.
     * @param errorList  A list containing possible detected errors before calling this method.
     */
    private void validateReviewBody(String reviewBody, List<ValueError> errorList) {
        Assert.notNull(errorList, "The error list must not be null");
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(reviewBody, NumericConstants.REVIEW_BODY_MIN_LENGTH,
                NumericConstants.TEXT_FIELD_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_REVIEW_BODY,
                ValueErrorConstants.REVIEW_BODY_TOO_SHORT, ValueErrorConstants.REVIEW_BODY_TOO_LONG);
    }

}