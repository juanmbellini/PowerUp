package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Models a thread, created by a specific user with a title, along with its comments and responses.
 */
@Entity
@Table(name = "threads")
public class Thread {

    private static final ValidationException.ValueError MISSING_CREATOR_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "creator", "A creator must be set.");

    private static final ValidationException.ValueError MISSING_TITLE_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "title", "A title must be set.");

    private static final ValidationException.ValueError ILLEGAL_EMPTY_TITLE_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "title", "The title must have at least one character.");


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
    private String initialComment;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "thread")
    @OrderBy("createdAt ASC")
    private Set<Comment> allComments;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "thread")
    private Set<ThreadLike> likes;

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar updatedAt;

    @Column(name = "hot_value")
    private double hotValue; // TODO: check how this is updated

    /*package*/  Thread() {
        this.allComments = new HashSet<>();
        this.likes = new HashSet<>();
        //for hibernate
    }

    /**
     * Creates a new thread.
     *
     * @param creator        The thread's creator.
     * @param title          The thread's title.
     * @param initialComment The thread's initial comment. May be empty, but not null.
     * @throws ValidationException If any value is wrong.
     */
    public Thread(User creator, String title, String initialComment) throws ValidationException {
        this();
        if (creator == null) {
            throw new ValidationException(Stream.of(MISSING_CREATOR_ERROR).collect(Collectors.toList()));
        }
        this.creator = creator;
        update(title, initialComment == null ? "" : initialComment);
        updateHotValue();
    }

    /**
     * Updates the thread.
     *
     * @param title          The new title.
     * @param initialComment The initial comment (i.e. body of the thread).
     * @throws ValidationException If any value is wrong.
     */
    public void update(String title, String initialComment) throws ValidationException {
        if (title == null) {
            throw new ValidationException(Stream.of(MISSING_TITLE_ERROR).collect(Collectors.toList()));
        } else if (title.isEmpty()) {
            throw new ValidationException(Stream.of(ILLEGAL_EMPTY_TITLE_ERROR).collect(Collectors.toList()));
        }
        this.title = title;
        this.initialComment = initialComment;
    }


    // TODO: javadoc, diego?
    public void updateHotValue() {
        long epoch = LocalDate.parse("2016-01-01").atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        int likeSize = (likes == null ? 0 : likes.size()) + 1;
        long millis = updatedAt == null ? System.currentTimeMillis() : updatedAt.getTimeInMillis();
        hotValue = (Math.log10(likeSize) + (millis / 1000 - epoch) / 45000.0);
    }

    public long getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public double getHotValue() {
        return hotValue;
    }


    public Collection<Comment> getAllComments() {
        return allComments;
    }

    @Transient
    public Collection<Comment> getTopLevelComments() {
        // Not caching into a variable since allComments may change and we have no way of tracking when this happens
        // to recompute all top-level comments.
        return allComments.stream()
                .filter(c -> c.getParentComment() == null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public int getLikeCount() {
        return likes.size();
    }

    public boolean isLikedBy(User user) {
        // TODO: what if one thread has 3M of likes and the given user's like is the last one?
        // TODO: what if the user didn't like the thread, and it has 40M likes?
        // TODO: I think it's better to do this using a query
        return likes.parallelStream().map(ThreadLike::getUser).collect(Collectors.toSet()).contains(user);
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

        Thread thread = (Thread) o;

        return id == thread.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Thread #" + id + ", creator=" + creator.getUsername()
                + ", title='" + title + "', initialComment='" + initialComment + "'";
    }

}
