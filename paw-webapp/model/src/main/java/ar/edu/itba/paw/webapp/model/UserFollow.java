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
public class UserFollow{

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
}
