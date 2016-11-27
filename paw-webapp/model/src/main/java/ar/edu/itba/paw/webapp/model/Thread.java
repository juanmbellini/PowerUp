package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Models a thread, created by a specific user with a title, along with its comments and responses.
 */
@Entity
@Table(name = "threads")
//@FilterDef(name = "onlyTopLevelComments", parameters = @ParamDef(name = "parentComment", type = ""))
public class Thread {

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
    @OrderBy("createdAt DESC")
//    @org.hibernate.annotations.Filter(name = "onlyTopLevelComments")
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

    /*package*/  Thread() {
        //for hibernate
    }

    /**
     * Creates a new thread.
     *
     * @param creator        The thread's creator.
     * @param title          The thread's title.
     * @param initialComment The thread's initial comment. May be empty, but not null.
     */
    public Thread(User creator, String title, String initialComment) {
        this.creator = creator;
        this.title = title;
        this.initialComment = initialComment;
    }

    /**
     * Creates a new thread with an empty initial comment.
     *
     * @param creator The thread's creator.
     * @param title   The thread's title.
     */
    public Thread(User creator, String title) {
        this(creator, title, "");
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(String initialComment) {
        this.initialComment = initialComment;
    }

    public Collection<Comment> getAllComments() {
        return allComments;
    }

    @Transient
    public Collection<Comment> getTopLevelComments() {
        //Not caching this into a variable since allComments may change and we have no way of tracking when this happens
        //to recompute all top-level comments.
        Set<Comment> result = new LinkedHashSet<>();
        for(Comment c : allComments) {
            if(c.getParentComment() == null) {
                result.add(c);
            }
        }
        return result;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public boolean isLikedBy(User user) {
        for(ThreadLike like : likes) {
            if(like.getUser().equals(user)) {
                return true;
            }
        }
        return false;
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
        return "Thread #" +
                id +
                ", creator=" + creator.getUsername() +
                ", title='" + title + '\'' +
                ", initialComment='" + initialComment + '\'';
    }
}
