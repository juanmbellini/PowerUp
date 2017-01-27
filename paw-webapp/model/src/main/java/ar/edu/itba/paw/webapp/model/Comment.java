package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Models a comment. A comment is part of a {@link Thread} and can have replies.
 */
@Entity
@Table(name = "comments")
public class Comment {

    private static final ValidationException.ValueError MISSING_THREAD_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "thread", "A creator must be set.");
    private static final ValidationException.ValueError MISSING_COMMENTER_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "commenter", "A title must be set.");
    private static final ValidationException.ValueError MISSING_COMMENT_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "comment", "A title must be set.");
    private static final ValidationException.ValueError ILLEGAL_EMPTY_COMMENT_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "comment", "The comment must have at least one character.");
    private static ValidationException.ValueError CYCLE_IN_PARENTS_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "parent", "A cycle was detected when updating the parent");


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
    private Set<Comment> replies = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comment")
    private Set<CommentLike> likes = new HashSet<>();

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
        update(thread, null, commenter, comment);
    }

    /**
     * Creates a new reply to a previous comment.
     *
     * @param parent  The parent comment that this comment is replying to.
     * @param replier The user who is replying.
     * @param comment The comment content.
     */
    public Comment(Comment parent, User replier, String comment) {
        update(parent.getThread(), parent, replier, comment);
    }


    public void update(Thread thread, Comment parent, User commenter, String comment) {
        List<ValidationException.ValueError> errors = new LinkedList<>();
        if (thread == null) {
            errors.add(MISSING_THREAD_ERROR);
        }
        if (!validParent(parent)) {
            errors.add(CYCLE_IN_PARENTS_ERROR);
        }
        if (commenter == null) {
            errors.add(MISSING_COMMENTER_ERROR);
        }
        if (comment == null) {
            errors.add(MISSING_COMMENT_ERROR);
        } else if (comment.isEmpty()) {
            errors.add(ILLEGAL_EMPTY_COMMENT_ERROR);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        this.thread = thread;
        this.parentComment = parent;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public boolean isLikedBy(User user) {
        // TODO: what if one comment has 3M of likes and the given user's like is the last one?
        // TODO: what if the user didn't like the comment, and it has 40M likes?
        // TODO: I think it's better to do this using a query
        return likes.parallelStream().map(CommentLike::getUser).collect(Collectors.toSet()).contains(user);
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


    /**
     * Checks if the parent is valid.
     *
     * @param parent The parent comment to check if it is valid.
     * @return {@code true} if it is a valid parent, or {@code false} otherwise.
     */
    private boolean validParent(Comment parent) {
        while (parent != null && !this.equals(parent)) {
            parent = parent.getParentComment();
        }
        // If parent is null then that was the cause of breaking off the cycle
        // If it's not null, the cause was that it was equals to "this", which must return false.
        return parent == null;
    }
}
