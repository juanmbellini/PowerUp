package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

/**
 * Models a comment. A comment is part of a {@link Thread} and can have replies.
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @SequenceGenerator(name = "comments_seq", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User commenter;

    @Column(name = "comment")
    private String comment;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "comments",
            joinColumns=@JoinColumn(name = "reply_to")
    )
    private Collection<Comment> replies;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "comment_likes",
            joinColumns=@JoinColumn(name = "comment_id")
    )
    private Collection<CommentLike> likes;

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar updatedAt;

    /*package*/ Comment() {
        //For Hibernate
    }

    public Comment(User commenter, String comment) {
        this.commenter = commenter;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public User getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public Collection<Comment> getReplies() {
        return replies;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
