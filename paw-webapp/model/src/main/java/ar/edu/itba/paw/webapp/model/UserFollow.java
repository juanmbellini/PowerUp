package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by julian on 16/07/17.
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

    public UserFollow(User followed, User follower) {
        final List<ValueError> errorList = new LinkedList<>();
        if (followed == null) {
            errorList.add(MISSING_FOLLOWED);
        }
        if (follower == null) {
            errorList.add(MISSING_FOLLOWER);
        }
        throwValidationException(errorList); // Throws ValidationException if any is null
        //noinspection ConstantConditions
        if (followed.getId() == follower.getId()) {
            throwValidationException(Collections.singletonList(AUTO_FOLLOW));
        }
        this.followed = followed;
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

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

    final private static ValueError MISSING_FOLLOWER = new ValueError(ValueError.ErrorCause.MISSING_VALUE,
            "follower", "Missing follower");

    final private static ValueError MISSING_FOLLOWED = new ValueError(ValueError.ErrorCause.MISSING_VALUE,
            "followed", "Missing followed");

    final private static ValueError AUTO_FOLLOW = new ValueError(ValueError.ErrorCause.ILLEGAL_VALUE,
            "followed", "Can't auto-follow");
}
