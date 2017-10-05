package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a relationship between two {@link User}s, where one follows the other one.
 */
@Entity
@Table(name = "user_follow",
        indexes = {@Index(name = "user_follow_pkey",
                columnList = "follower_id, followed_id", unique = true)})
public class UserFollow implements ValidationExceptionThrower {

    @Id
    @SequenceGenerator(name = "user_follow_seq", sequenceName = "user_follow_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_follow_seq")
    private long id;

    /**
     * The {@link User} being followed.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "followed_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User followed;

    /**
     * The {@link User} following.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "follower_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User follower;

    @Column(name = "date")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar date;

    public UserFollow() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param followed The {@link User} being followed.
     * @param follower The {@link User} following.
     * @throws ValidationException If any validation error occurs.
     */
    public UserFollow(User followed, User follower) throws ValidationException {
        final List<ValueError> errorList = new LinkedList<>();
        // Check if any is null
        ValidationHelper.objectNotNull(followed, errorList, ValueErrorConstants.MISSING_FOLLOWED);
        ValidationHelper.objectNotNull(follower, errorList, ValueErrorConstants.MISSING_FOLLOWER);
        throwValidationException(errorList); // Throws ValidationException if any is null

        // If both are not null, check that this is not an auto-follow
        if (followed.getId() == follower.getId()) {
            throwValidationException(Collections.singletonList(ValueErrorConstants.AUTO_FOLLOW));
        }

        this.followed = followed;
        this.follower = follower;
    }

    /**
     * Followed getter.
     *
     * @return The {@link User} being followed.
     */
    public User getFollowed() {
        return followed;
    }

    /**
     * Follower getter.
     *
     * @return The {@link User} following.
     */
    public User getFollower() {
        return follower;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollow)) return false;

        UserFollow that = (UserFollow) o;

        return (followed == null ? that.followed == null : followed.equals(that.followed))
                && (follower == null ? that.follower == null : follower.equals(that.follower));
    }

    @Override
    public int hashCode() {
        int result = follower != null ? follower.hashCode() : 0;
        result = 31 * result + (followed != null ? followed.hashCode() : 0);
        return result;
    }
}
