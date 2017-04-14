package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
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

@Repository
public class ThreadHibernateDao implements ThreadDao {


    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Thread> getThreads(String titleFilter, Long userIdFilter, String userNameFilter,
                                   int pageNumber, int pageSize,
                                   SortingType sortingType, SortDirection sortDirection) {

        if ((titleFilter != null && titleFilter.isEmpty()) || (userIdFilter != null && userIdFilter <= 0)
                || userNameFilter != null && userNameFilter.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final StringBuilder query = new StringBuilder().append("FROM Thread");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        if (titleFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("title = :threadTitle",
                    "%" + titleFilter + "%"));
        }

        if (userIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.id = :userId", userIdFilter));
        }
        if (userNameFilter != null && !userNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.username = :userName",
                    "%" + userNameFilter + "%"));
        }
        DaoHelper.appendConditions(query, conditions);

        // Sorting
        query.append(" ORDER BY ").append(sortingType.getFieldName()).append(" ").append(sortDirection.getQLKeyword());

        return DaoHelper.findPageWithConditions(em, Thread.class, pageNumber, pageSize, query.toString(),
                conditions.stream().map(DaoHelper.ConditionAndParameterWrapper::getParameter).toArray());
    }

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
