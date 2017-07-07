package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.LikeableEntity;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Models a thread, created by a specific user with a title, along with its comments and responses.
 */
@Entity
@Table(name = "threads")
public class Thread implements ValidationExceptionThrower, LikeableEntity {

    @Id
    @SequenceGenerator(name = "threads_seq", sequenceName = "threads_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threads_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User creator;

    @Column(name = "title")
    private String title;

    @Column(name = "initial_comment")
    private String body;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Comment> allComments; // Used just for counting

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<ThreadLike> likes; // Used just for counting

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar updatedAt;

    @Column(name = "hot_value")
    private double hotValue;


    /* package */  Thread() {
        this.allComments = new HashSet<>();
        this.likes = new HashSet<>();
        // For Hibernate
    }

    /**
     * Creates a new thread.
     *
     * @param creator The thread's creator.
     * @param title   The thread's title.
     * @param body    The thread's initial comment. May be empty, but not null.
     * @throws ValidationException If any value is wrong.
     */
    public Thread(User creator, String title, String body) throws ValidationException {
        this();
        final List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(creator, errorList, ValueErrorConstants.MISSING_CREATOR);

        update(title, body, errorList);
        this.creator = creator;
        updateHotValue();
    }

    /**
     * Updates the thread.
     *
     * @param title The new title.
     * @param body  The initial comment (i.e. body of the thread).
     * @throws ValidationException If any value is wrong.
     */
    public void update(String title, String body) throws ValidationException {
        update(title, body, new LinkedList<>());
    }

    /**
     * Updates the thread, receiving a list of detected errors before executing this method.
     *
     * @param title The new title.
     * @param body  The initial comment (i.e. body of the thread).
     * @throws ValidationException If any value is wrong.
     */
    private void update(String title, String body, List<ValueError> errorList) throws ValidationException {
        checkValues(title, body, errorList);
        this.title = title;
        this.body = body;
    }


    // TODO: javadoc, diego?
    public void updateHotValue() {
        long epoch = LocalDate.parse("2016-01-01").atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        int likeSize = (likes == null ? 0 : likes.size()) + 1;
        long millis = updatedAt == null ? System.currentTimeMillis() : updatedAt.getTimeInMillis();
        hotValue = (Math.log10(likeSize) + (millis / 1000 - epoch) / 45000.0);
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
     * Creator getter.
     *
     * @return The creator.
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Title getter.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Initial comment getter.
     *
     * @return The initial comment (i.e the thread's body).
     */
    public String getBody() {
        return body;
    }


    /**
     * Hot value getter.
     *
     * @return The hot value.
     */
    public double getHotValue() {
        return hotValue;
    }


// TODO: move to query
//    /**
//     * All comments getter.
//     *
//     * @return A collection containing all the comments of this thread.
//     */
//    public Collection<Comment> getAllComments() {
//        return allComments;
//    }
//
//    /**
//     * Top level comments getter.
//     *
//     * @return A collection containing top level comments of this thread.
//     */
//    @Transient
//    public Collection<Comment> getTopLevelComments() {
//        // Not caching into a variable since allComments may change and we have no way of tracking when this happens
//        // to recompute all top-level comments.
//        return allComments.stream()
//                .filter(c -> c.getParentComment() == null)
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//    }
//

    /**
     * Likes count getter.
     *
     * @return The amount of likes.
     */
    public long getLikeCount() {
        return likes.size();
    }

    /**
     * Comments count getter.
     *
     * @return The amount of comments.
     */
    public long getCommentCount() {
        return allComments.size();
    }


// TODO: move to query
//    /**
//     * Indicates whether this thread is liked by the given {@link User}.
//     *
//     * @param user The {@link User} to be checked whether they liked the thread.
//     * @return {@code true} if the given {@link User} liked the thread, or {@code false} otherwise.
//     */
//    public boolean isLikedBy(User user) {
//        // TODO: what if one thread has 3M of likes and the given user's like is the last one?
//        // TODO: what if the user didn't like the thread, and it has 40M likes?
//        // TODO: I think it's better to do this using a query
////        return likes.parallelStream().map(ThreadLike::getUser).collect(Collectors.toSet()).contains(user);
//        return likes.contains(new ThreadLike(user, this));
//    }
//
//    public boolean isLikedBy(ThreadLike threadLike) {
//        return likes.contains(threadLike);
//    }

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

        Thread thread = (Thread) o;

        return id == thread.id;
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

    @Override
    public String toString() {
        return "Thread #" + id + ", creator=" + creator.getUsername()
                + ", title='" + title + "', body='" + body + "'";
    }


    /**
     * Checks the given values, throwing a {@link ValidationException} if any is wrong.
     *
     * @param title          The title to be checked.
     * @param initialComment The initial comment to be checked.
     * @param errorList      A list containing possible detected errors before calling this method.
     * @throws ValidationException If any value is wrong.
     */
    private void checkValues(String title, String initialComment, List<ValueError> errorList)
            throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(title, NumericConstants.TITLE_MIN_LENGTH,
                NumericConstants.TITLE_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_TITLE,
                ValueErrorConstants.TITLE_TOO_SHORT, ValueErrorConstants.TITLE_TOO_LONG);
        ValidationHelper.stringNullOrLengthBetweenTwoValues(initialComment, NumericConstants.THREAD_BODY_MIN_LENGTH,
                NumericConstants.TEXT_FIELD_MAX_LENGTH, errorList, ValueErrorConstants.THREAD_BODY_TOO_SHORT,
                ValueErrorConstants.THREAD_BODY_TOO_LONG);
        throwValidationException(errorList);
    }

}
