package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class ThreadHibernateDao implements ThreadDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Thread create(String title, long creatorUserId, String creatorComment) throws NoSuchEntityException {
        User creator = DaoHelper.findSingleOrThrow(em, User.class, creatorUserId);
        Thread thread = new Thread(creator, title, creatorComment);
        em.persist(thread);
        return thread;
    }

    @Override
    public void update(Thread thread, String title, String initialComment) {
        if (thread == null) {
            throw new IllegalArgumentException("The thread can not be null.");
        }
        thread.update(title, initialComment);
        em.merge(thread);
    }

    @Override
    public void deleteThread(Thread thread) throws NoSuchEntityException {
        if (thread == null) {
            throw new IllegalArgumentException("The thread can not be null.");
        }
        em.remove(thread);
    }

    @Override
    public void updateHotValue(Thread thread) {
        if (thread == null) {
            throw new IllegalArgumentException("The thread can not be null.");
        }
        thread.updateHotValue();
        em.merge(thread);
    }


    @Override
    public Set<Thread> findRecent(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY T.createdAt DESC"));
    }

    @Override
    public Set<Thread> findBestPointed(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY SIZE(likes) DESC"));
    }

    @Override
    public Set<Thread> findHottest(int limit) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditionsAndLimit(em, Thread.class, limit, "FROM Thread AS T ORDER BY T.hotValue DESC"));
    }

    @Override
    public Set<Thread> findByUserId(long id) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditions(em, Thread.class, "FROM Thread AS T WHERE T.user.id = ?1", id));
    }

    @Override
    public Set<Thread> findByTitle(String title) {
        return new LinkedHashSet<>(DaoHelper.findManyWithConditions(em, Thread.class, "FROM Thread AS T WHERE T.title = ?1", title));
    }

    @Override
    public Thread findById(long threadId) {
        return DaoHelper.findSingle(em, Thread.class, threadId);
    }


}
