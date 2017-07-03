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
        indexes = {@Index(name = "thread_likes_pkey",
                columnList = "user_id, thread_id", unique = true)})
public class ThreadLike implements ValidationExceptionThrower {

    @Id
    @SequenceGenerator(name = "thread_likes_seq", sequenceName = "thread_likes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thread_likes_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "thread_id",
            referencedColumnName = "id",
            nullable = false
    )
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreadLike)) return false;

        ThreadLike that = (ThreadLike) o;

        return (user == null ? that.user == null : user.equals(that.user))
                && (thread == null ? that.thread == null : thread.equals(that.thread));

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (thread != null ? thread.hashCode() : 0);
        return result;
    }

}
