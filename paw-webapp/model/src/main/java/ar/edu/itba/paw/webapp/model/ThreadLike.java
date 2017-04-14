package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Models a {@link Thread} like. Used to track likes and to avoid users from liking threads multiple times.
 */
@Entity
@Table(name = "thread_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "thread_id"}))
public class ThreadLike implements ValidationExceptionThrower {

    @Id
    @SequenceGenerator(name = "thread_likes_seq", sequenceName = "thread_likes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thread_likes_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Thread thread;

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    /*package*/ ThreadLike() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param user   The {@link User} liking the thread.
     * @param thread The liked {@link Thread}.
     * @throws ValidationException If any value is wrong.
     */
    public ThreadLike(User user, Thread thread) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(thread, errors, ValueErrorConstants.MISSING_THREAD);
        ValidationHelper.objectNotNull(user, errors, ValueErrorConstants.MISSING_USER);

        throwValidationException(errors);
        this.user = user;
        this.thread = thread;
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
     * User getter.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
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
     * Created at getter.
     *
     * @return The moment this object is created.
     */
    public Calendar getCreatedAt() {
        return createdAt;
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

        ThreadLike thread = (ThreadLike) o;

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
}
