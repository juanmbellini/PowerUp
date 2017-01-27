package ar.edu.itba.paw.webapp.model;

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
public class ThreadLike {

    private static final ValidationException.ValueError MISSING_THREAD_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "thread", "A thread must be set.");
    private static final ValidationException.ValueError MISSING_USER_ERROR =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "user", "An user must be set.");

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
        //For Hibernate
    }

    public ThreadLike(User user, Thread thread) {
        List<ValidationException.ValueError> errors = new LinkedList<>();
        if (thread == null) {
            errors.add(MISSING_THREAD_ERROR);
        }
        if (user == null) {
            errors.add(MISSING_USER_ERROR);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        this.user = user;
        this.thread = thread;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Thread getThread() {
        return thread;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadLike thread = (ThreadLike) o;

        return id == thread.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
