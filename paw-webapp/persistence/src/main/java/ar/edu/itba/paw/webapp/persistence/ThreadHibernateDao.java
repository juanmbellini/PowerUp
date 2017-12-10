package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class ThreadHibernateDao implements ThreadDao {


    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Thread> getThreads(String titleFilter, Long userIdFilter, String usernameFilter,
                                   int pageNumber, int pageSize,
                                   SortingType sortingType, SortDirection sortDirection) {
        // First we sanitize the string values.
        titleFilter = Optional.ofNullable(titleFilter).map(DaoHelper::escapeUnsafeCharacters).orElse(null);
        usernameFilter = Optional.ofNullable(usernameFilter).map(DaoHelper::escapeUnsafeCharacters).orElse(null);

        final StringBuilder query = new StringBuilder()
                .append("FROM Thread thread");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        int conditionNumber = 0;
        if (titleFilter != null && !titleFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(title) LIKE ?" + conditionNumber,
                    "%" + titleFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (userIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("creator.id = ?" + conditionNumber,
                    userIdFilter, conditionNumber));
            conditionNumber++;
        }
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(creator.username) LIKE ?" + conditionNumber,
                    "%" + usernameFilter.toLowerCase() + "%", conditionNumber));
        }

        return DaoHelper.findPageWithConditions(em, Thread.class, query, "thread", "thread.id", conditions,
                pageNumber, pageSize, sortingType.getFieldName(), sortDirection, false);
    }


    @Override
    public Thread create(String title, String creatorComment, User creator) {
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
    public void delete(Thread thread) {
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
    public Thread findById(long threadId) {
        return DaoHelper.findSingle(em, Thread.class, threadId);
    }


}
