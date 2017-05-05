package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.utilities.Page;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * DAO helper class. Provides package-private static methods useful to all DAOs.
 */
/*package*/ class DaoHelper {

    /**
     * Attempts to find an entity by ID. If found, returns it, otherwise returns {@code null}.
     *
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the entity to find.
     * @param id            The ID of the entity to find.
     * @return The found entity, or {@code null} if not found.
     */
    /*package*/
    static <T> T findSingle(EntityManager entityManager, Class<T> klass, long id) {
        return entityManager.find(klass, id);
    }

    /**
     * Attempts to find an entity by ID. If found, returns it, otherwise throws exception.
     *
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the entity to find.
     * @param id            The ID of the entity to find.
     * @return The found entity.
     * @throws NoSuchEntityException If no such entity exists.
     */
    /*package*/
    static <T> T findSingleOrThrow(EntityManager entityManager, Class<T> klass, long id) throws NoSuchEntityException {
        T result = findSingle(entityManager, klass, id);
        if (result == null) {
            throw new NoSuchEntityException(klass, id);
        }
        return result;
    }

    /**
     * Performs a basic search with parameters, returning a single result.
     *
     * @param <T>           The return type.
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the object type to return.
     * @param baseQuery     The base HQL query. <b>NOTE:</b> Parameters must be numbered instead of named,
     *                      e.g. {@code "FROM User AS U WHERE U.username = ?1}"
     * @param parameters    Parameters for the query, in order of appearance in the query.
     * @return The found entity, or {@code null} if not found.
     * @throws javax.persistence.NonUniqueResultException If more than one result is found.
     */
    /*package*/
    static <T> T findSingleWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery, Object... parameters) throws NonUniqueResultException {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Attempts to find a all entities of a specified class without specifying a sorting criterion.
     *
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the entities to find.
     * @return The found entities.
     */
    /*package*/
    static <T> List<T> findAll(EntityManager entityManager, Class<T> klass) {
        TypedQuery<T> query = entityManager.createQuery("FROM " + klass.getName(), klass);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Performs a basic search with parameters, returning a list of results.
     *
     * @param <T>           The return type.
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the object type to return.
     * @param baseQuery     The base HQL query. <b>NOTE:</b> Parameters must be numbered instead of named,
     *                      e.g. {@code "FROM User AS U WHERE U.username LIKE ?1}"
     * @param parameters    Parameters for the query, in order of appearance in the query.
     * @return A list with the matching entities, or an empty list of nothing is found if not found.
     */
    /*package*/
    static <T> List<T> findManyWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Performs a basic search with parameters, returning a list of a limited number of results.
     *
     * @param <T>           The return type.
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the object type to return.
     * @param limit         The maximum number of results to fetch.
     * @param baseQuery     The base HQL query. <b>NOTE:</b> Parameters must be numbered instead of named,
     *                      e.g. {@code "FROM User AS U WHERE U.username LIKE ?1}"
     * @param parameters    Parameters for the query, in order of appearance in the query.
     * @return A list with the matching entities, or an empty list of nothing is found if not found.
     */
    /*package*/
    static <T> List<T> findManyWithConditionsAndLimit(EntityManager entityManager, Class<T> klass, int limit, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.setMaxResults(limit);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Performs a basic search with parameters, returning a page of results.
     *
     * @param <T>           The return type.
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the object type to return.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param baseQuery     The base HQL query. <b>NOTE:</b> Parameters must be numbered instead of named,
     *                      e.g. {@code "FROM User AS U WHERE U.username LIKE ?1}"
     * @param parameters    Parameters for the query, in order of appearance in the query.
     * @return A list with the matching entities, or an empty list of nothing is found if not found.
     */
    /*package*/
    static <T> Page<T> findPageWithConditions(EntityManager entityManager, Class<T> klass, int pageNumber, int pageSize, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        try {
            Page<T> page = new Page<>();
            List<T> data = findManyWithConditions(entityManager, klass, baseQuery, parameters);
            page.setTotalPages(Math.max((int) Math.ceil(data.size() / pageSize), 1));
            page.setPageSize(pageSize);
            page.setPageNumber(pageNumber);
            page.setOverAllAmountOfElements(data.size());
            page.setData(query.getResultList());
            return page;
        } catch (NoResultException e) {
            return Page.emptyPage();
        }
    }


    /***
     * Checks for existence of an entity using COUNT with the entity's ID.
     *
     * @param em The entity manager
     * @param klass The entity's class
     * @param id The entity's ID
     * @param <T> The entity class as a type parameter
     * @return Whether such entity exists.
     */
    /*package*/
    static <T> boolean exists(EntityManager em, Class<T> klass, long id) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM " + klass.getName() + " WHERE id = ?1", Long.class);
        query.setParameter(1, id);
        return query.getSingleResult() > 0;
    }

    /***
     * Checks for existence of an entity using COUNT with conditions.  For ID-based search, see {@link #exists(EntityManager, Class, long)}.
     *
     * @param em The entity manager
     * @param query The query to execute. Can be prefixed with {@code "SELECT COUNT(*)"} or not.
     * @param params The query parameters.
     * @return Whether such entity exists.
     */
    /*package*/
    static boolean exists(EntityManager em, String query, Object... params) {
        if (!query.toUpperCase().startsWith("SELECT COUNT")) {
            query = "SELECT COUNT(*) " + query;
        }
        //noinspection ConstantConditions - The count should never return null
        return findSingleWithConditions(em, Long.class, query, params) > 0;
    }


    static <T> Page<T> findPageWithConditions(EntityManager em, Class<T> klass,
                                              StringBuilder baseQuery, String baseAlias, String countField,
                                              List<ConditionAndParameterWrapper> conditions,
                                              int pageNumber, int pageSize, String sortByField,
                                              SortDirection sortDirection) {
        // Conditions
        appendConditions(baseQuery, conditions);

        StringBuilder countQueryStr = new StringBuilder()
                .append("SELECT COUNT(DISTINCT ").append(countField).append(") ")
                .append(baseQuery);

        TypedQuery<Long> countQuery = em.createQuery(countQueryStr.toString(), Long.class);
        conditions.forEach(wrapper -> countQuery.setParameter(wrapper.getPosition(), wrapper.getParameter()));
        long count = countQuery.getSingleResult();
        if (count == 0) {
            return Page.emptyPage();
        }

        StringBuilder dataQueryStr = new StringBuilder()
                .append("SELECT DISTINCT ").append(baseAlias).append(" ")
                .append(baseQuery)
                .append(" ORDER BY ").append(sortByField).append(" ").append(sortDirection.getQLKeyword());

        TypedQuery<T> dataQuery = em.createQuery(dataQueryStr.toString(), klass)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize);
        conditions.forEach(wrapper -> dataQuery.setParameter(wrapper.getPosition(), wrapper.getParameter()));

        return createPage(dataQuery.getResultList(), pageSize, pageNumber, count);

    }


    /**
     * Creates a page.
     *
     * @param data                    Data in the page.
     * @param pageSize                Size of the page.
     * @param pageNumber              Number of page.
     * @param overAllAmountOfElements Amount of elements in all pages.
     * @param <T>                     Type of data.
     * @return A page built from the given params.
     */
    private static <T> Page<T> createPage(Collection<T> data, int pageSize, int pageNumber,
                                          long overAllAmountOfElements) {
        Page<T> page = new Page<T>();
        page.setTotalPages(Math.max((int) Math.ceil((double) overAllAmountOfElements / pageSize), 1));
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setOverAllAmountOfElements(overAllAmountOfElements);
        page.setData(data);
        return page;

        // TODO: use page builder when merging
//        return new Page.Builder<T>()
//                .setPageSize(pageSize)
//                .setPageNumber(pageNumber)
//                .setOverAllAmountOfElements(overAllAmountOfElements)
//                .setTotalPages(Math.max((int) Math.ceil((double) overAllAmountOfElements / pageSize), 1))
//                .setData(data).build();
    }

    /**
     * Appends the given {@code conditions} to the given {@code query}.
     *
     * @param query      The query.
     * @param conditions The conditions.
     */
    /* package */
    static void appendConditions(StringBuilder query, List<ConditionAndParameterWrapper> conditions) {
        if (!conditions.isEmpty()) {
            int i = 0;
            query.append(" WHERE ").append(conditions.get(i++).getCondition());
            while (i < conditions.size()) {
                query.append(" AND ").append(conditions.get(i++).getCondition());
            }
        }
    }


    /**
     * Encapsulates a condition in HQL, and a parameter to be used in the condition.
     */
    /* package */ final static class ConditionAndParameterWrapper {
        /**
         * Contains the condition in HQL.
         */
        private final String condition;
        /**
         * Object to be used as parameter.
         */
        private final Object parameter;

        private int position;


        /**
         * Constructor.
         *
         * @param condition The condition in HQL.
         * @param parameter The object to be used as parameter.
         */
        @Deprecated
        /* package */ ConditionAndParameterWrapper(String condition, Object parameter) {
            this.condition = condition;
            this.parameter = parameter;
        }

        /* package */ ConditionAndParameterWrapper(String condition, Object parameter, int position) {
            this(condition, parameter);
            this.position = position;
        }


        /**
         * Condition getter.
         *
         * @return The condition in HQL.
         */
        public String getCondition() {
            return condition;
        }

        /**
         * Parameter getter.
         *
         * @return The object to be used as parameter.
         */
        public Object getParameter() {
            return parameter;
        }

        public int getPosition() {
            return position;
        }
    }
}

