package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.Commentable;
import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Models a comment. A comment is part of a {@link Thread} and can have replies.
 */
@Entity
@Table(name = "comments")
public class Comment implements ValidationExceptionThrower, Likeable, Commentable {


    @Id
    @SequenceGenerator(name = "comments_seq", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User commenter;

    @Column(name = "comment")
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    private Thread thread;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Comment> replies = new HashSet<>();

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
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
        // For Hibernate
    }

    /**
     * Creates a new top-level comment.
     * This constructor is meant to be used for first comment.
     *
     * @param thread    The thread this comment belongs to.
     * @param body      The comment content.
     * @param commenter The user who is commenting.
     * @throws ValidationException If any value is wrong.
     */
    public Comment(Thread thread, String body, User commenter) throws ValidationException {
        final List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(thread, errors, ValueErrorConstants.MISSING_THREAD);
        ValidationHelper.objectNotNull(commenter, errors, ValueErrorConstants.MISSING_COMMENTER);

        update(body, errors);
        this.thread = thread;
        this.parentComment = null;
        this.commenter = commenter;
    }

    /**
     * Creates a new reply to a previous comment.
     * This constructor is meant to be used for replies.
     *
     * @param parent  The parent comment that this comment is replying to.
     * @param body    The comment content.
     * @param replier The user who is replying.
     * @throws ValidationException If any value is wrong.
     */
    public Comment(Comment parent, String body, User replier) throws ValidationException {
        final List<ValueError> errors = new LinkedList<>();
        if (parent == null) {
            errors.add(ValueErrorConstants.MISSING_PARENT); // This constructor is used for replies
        } else {
            if (!validParent(parent)) {
                errors.add(ValueErrorConstants.CYCLE_IN_PARENTS);
            }
            ValidationHelper.objectNotNull(parent.getThread(), errors, ValueErrorConstants.MISSING_THREAD);
        }
        ValidationHelper.objectNotNull(replier, errors, ValueErrorConstants.MISSING_COMMENTER);

        update(body, errors);
        //noinspection ConstantConditions
        this.thread = parent.getThread();
        this.parentComment = parent;
        this.commenter = replier;
    }

    /**
     * Updates the comment.
     *
     * @param body The new body.
     * @throws ValidationException If any value is wrong.
     */
    public void update(String body) throws ValidationException {
        update(body, new LinkedList<>());
    }

    /**
     * Updates the comment, receiving a list of detected errors before executing this method.
     *
     * @param body      The new body.
     * @param errorList The list containing possible errors detected before executing the method.
     * @throws ValidationException If any value is wrong.
     */
    private void update(String body, List<ValueError> errorList) throws ValidationException {
        checkValues(body, errorList);
        this.body = body;
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
     * Commenter getter.
     *
     * @return The body.
     */
    public User getCommenter() {
        return commenter;
    }

    /**
     * Comment getter.
     *
     * @return The body (i.e content of comment).
     */
    public String getBody() {
        return body;
    }

    /**
     * Likes count getter.
     *
     * @return The amount of likes.
     */
    public long getLikeCount() {
        return likes.size();
    }

    /**
     * Indicates whether this comment is liked by the given {@link User}.
     *
     * @param user The {@link User} to be checked whether they liked the comment.
     * @return {@code true} if the given {@link User} liked the comment, or {@code false} otherwise.
     */
    public boolean isLikedBy(User user) {
        // TODO: what if one comment has 3M of likes and the given user's like is the last one?
        // TODO: what if the user didn't like the comment, and it has 40M likes?
        // TODO: I think it's better to do this using a query
        return likes.parallelStream().map(CommentLike::getUser).collect(Collectors.toSet()).contains(user);
    }

    /**
     * Replies getter.
     *
     * @return A collection containing the replies to this comment.
     */
    public Collection<Comment> getReplies() {
        return replies;
    }

    /**
     * Thread getter.
     *
     * @return The thread.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Parent comment getter.
     *
     * @return The parent comment.
     */
    public Comment getParentComment() {
        return parentComment;
    }

    @Override
    public long getCommentCount() {
        return replies.size();
    }

    /**
     * Created at getter.
     *
     * @return The moment at which this comment was created.
     */
    public Calendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Updated at getter.
     *
     * @return The moment at which this comment was updated.
     */
    public Calendar getUpdatedAt() {
        return updatedAt;
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

        Comment comment = (Comment) o;

        return id == comment.id;
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
     * Checks the given values, throwing a {@link ValidationException} if any is wrong.
     *
     * @param body      The body to be checked.
     * @param errorList A list containing possible detected errors before calling this method.
     * @throws ValidationException If any value is wrong.
     */
    private void checkValues(String body, List<ValueError> errorList) throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(body, NumericConstants.COMMENT_BODY_MIN_LENGTH,
                NumericConstants.TEXT_FIELD_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_COMMENT,
                ValueErrorConstants.COMMENT_TOO_SHORT, ValueErrorConstants.COMMENT_TOO_LONG);
        throwValidationException(errorList);
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
