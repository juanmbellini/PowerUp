package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private Thread thread;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private Set<Comment> replies;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comment")
    private Set<CommentLike> likes;

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

    /**
     * Creates a new top-level comment.
     *
     * @param thread    The thread this comment belongs to.
     * @param commenter The user who is commenting.
     * @param comment   The comment content.
     */
    public Comment(Thread thread, User commenter, String comment) {
        this.thread = thread;
        this.parentComment = null;
        this.commenter = commenter;
        this.comment = comment;
    }

    /**
     * Creates a new reply to a previous comment.
     *
     * @param parent  The parent comment that this comment is replying to.
     * @param replier The user who is replying.
     * @param comment The comment content.
     */
    public Comment(Comment parent, User replier, String comment) {
        this.thread = parent.getThread();
        this.parentComment = parent;
        this.commenter = replier;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public boolean isLikedBy(User user) {
        for(CommentLike like : likes) {
            if(like.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public Collection<Comment> getReplies() {
        return replies;
    }

    public Thread getThread() {
        return thread;
    }

    public Comment getParentComment() {
        return parentComment;
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
