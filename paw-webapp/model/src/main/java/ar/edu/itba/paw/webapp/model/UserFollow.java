package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by julian on 16/07/17.
 */
@Entity
@Table(name = "user_follow",
        indexes = {@Index(name = "user_follow_pkey",
                columnList = "follower_id, followed_id", unique = true)})
public class UserFollow {

    @Id
    @SequenceGenerator(name = "user_follow_seq", sequenceName = "user_follow_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_follow_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "followed_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User followed;

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

    public UserFollow(User followed, User follower) {
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
}
