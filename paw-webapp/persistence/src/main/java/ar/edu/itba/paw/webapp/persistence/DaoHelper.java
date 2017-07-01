package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NumberOfPageBiggerThanTotalAmountException;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.utilities.Page;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.IntStream;

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
     * Returns the object of the given {@code klass} with the given {@code fieldName} set to the given {@code value}.
     * If more than one object matches, the first one is returned.
     * If no object matches, {@code null} is returned.
     *
     * @param em        The entity manager.
     * @param klass     The class of the returned object.
     * @param fieldName The field name to use as a search criteria
     * @param value     The value of the field.
     * @param <T>       The type of the object to be returned.
     * @param <E>       The type of the value
     * @return The first element with the given {@code fieldName} set to {@code value}, or {@code null} if not present.
     */
    /* package*/
    static <T, E> T findByField(EntityManager em, Class<T> klass, String fieldName, E value) {
        List<T> result = em.createQuery("FROM " + klass.getSimpleName() +
                " WHERE " + fieldName + " = ?0", klass).setParameter(0, value).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Returns the object of the given {@code klass} that matches the given {@code fieldsAndValues}.
     * If more than one object matches, the first one is returned.
     * If no object matches, {@code null} is returned.
     *
     * @param em              The entity manager.
     * @param klass           The class of the returned object.
     * @param fieldsAndValues A map containing field names as keys and values for those fields as values.
     * @param <T>             The type of the object to be returned.
     * @return The first element matching the given {@code fieldsAndValues}, or {@code null} if not present.
     */
    /* package */
    static <T> T findByFields(EntityManager em, Class<T> klass, Map<String, Object> fieldsAndValues) {
        if (fieldsAndValues == null) {
            throw new IllegalArgumentException();
        }
        // Create query string
        StringBuilder str = new StringBuilder().append("FROM ").append(klass.getSimpleName());
        Iterator<Map.Entry<String, Object>> it = fieldsAndValues.entrySet().iterator();
        int paramNumber = 0;
        Map<Integer, Object> params = new HashMap<>();
        if (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            str.append(" WHERE ").append(entry.getKey()).append(" = ?").append(paramNumber);
            params.put(paramNumber, entry.getValue());
            paramNumber++;
        }
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            str.append(" AND ").append(entry.getKey()).append(" = ?").append(paramNumber);
            params.put(paramNumber, entry.getValue());
            paramNumber++;
        }

        final TypedQuery<T> query = em.createQuery(str.toString(), klass);
        params.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        List<T> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
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
    // TODO: what's the purpose of throwing an exception when not finding the entity. Null can be checked and is better
    static <T> T findSingleOrThrow(EntityManager entityManager, Class<T> klass, long id)
            throws NoSuchEntityException {
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
    static <T> T findSingleWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery,
                                          Object... parameters) throws NonUniqueResultException {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        IntStream.range(0, parameters.length).forEach(each -> query.setParameter(each + 1, parameters[each]));
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
        return entityManager.createQuery("FROM " + klass.getName(), klass).getResultList();
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
    static <T> List<T> findManyWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery,
                                              Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        IntStream.range(0, parameters.length).forEach(each -> query.setParameter(each + 1, parameters[each]));
        return query.getResultList();
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
    static <T> List<T> findManyWithConditionsAndLimit(EntityManager entityManager, Class<T> klass,
                                                      int limit, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass).setMaxResults(limit);
        IntStream.range(0, parameters.length).forEach(each -> query.setParameter(each + 1, parameters[each]));
        return query.getResultList();
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
    static <T> Page<T> findPageWithConditions(EntityManager entityManager, Class<T> klass, int pageNumber,
                                              int pageSize, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        IntStream.range(0, parameters.length).forEach(each -> query.setParameter(each + 1, parameters[each]));
        List<T> allData = query.getResultList(); // Returns all results without limiting
        return allData.isEmpty() ? Page.emptyPage() : new Page.Builder<T>()
                .setPageNumber(pageNumber)
                .setOverAllAmountOfElements(allData.size())
                .setTotalPages(Math.max((int) Math.ceil(allData.size() / pageSize), 1))
                .setPageSize(pageSize)
                .setData(query
                        .setFirstResult((pageNumber - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList()) // Returns data applying limits
                .build();
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
        //TODO suffix with LIMIT 1 if necessary or use findPage with limit 1
        //noinspection ConstantConditions - The count should never return null
        return findSingleWithConditions(em, Long.class, query, params) > 0;
    }


    /**
     * Retrieves a {@link Page} applying conditions
     *
     * @param em            The entity manager.
     * @param klass         The entity's class.
     * @param baseQuery     The base query.
     * @param baseAlias     The alias used for the entity in the base query.
     * @param countField    The field used to count.
     * @param conditions    A list containing conditions to be applied.
     *                      Check {@link DaoHelper.ConditionAndParameterWrapper}
     * @param pageNumber    The number of page.
     * @param pageSize      The size of page.
     * @param sortByField   The field used to sort.
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @param <T>           The type of the entity.
     * @return The page containing matched data.
     */
    static <T> Page<T> findPageWithConditions(EntityManager em, Class<T> klass,
                                              String baseQuery, String baseAlias, String countField,
                                              List<ConditionAndParameterWrapper> conditions,
                                              int pageNumber, int pageSize, String sortByField,
                                              SortDirection sortDirection) {
        return findPageWithConditions(em, klass, new StringBuilder(baseQuery), baseAlias, countField, conditions,
                pageNumber, pageSize, sortByField, sortDirection);

    }


    /**
     * Retrieves a {@link Page} applying conditions
     *
     * @param em            The entity manager.
     * @param klass         The entity's class.
     * @param baseQuery     A {@link StringBuilder} containing the base query.
     * @param baseAlias     The alias used for the entity in the base query.
     * @param countField    The field used to count.
     * @param conditions    A list containing conditions to be applied.
     *                      Check {@link DaoHelper.ConditionAndParameterWrapper}
     * @param pageNumber    The number of page.
     * @param pageSize      The size of page.
     * @param sortByField   The field used to sort.
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @param <T>           The type of the entity.
     * @return The page containing matched data.
     */
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

        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / pageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }

        StringBuilder dataQueryStr = new StringBuilder()
                .append("SELECT DISTINCT ").append(baseAlias).append(" ")
                .append(baseQuery)
                .append(" ORDER BY ").append(sortByField).append(" ").append(sortDirection.getQLKeyword());

        TypedQuery<T> dataQuery = em.createQuery(dataQueryStr.toString(), klass)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize);
        conditions.forEach(wrapper -> dataQuery.setParameter(wrapper.getPosition(), wrapper.getParameter()));

        return createPage(dataQuery.getResultList(), pageSize, pageNumber, totalAmountOfPages, count);

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
        return createPage(data, pageSize, pageNumber,
                Math.max((int) Math.ceil((double) overAllAmountOfElements / pageSize), 1), overAllAmountOfElements);
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
    private static <T> Page<T> createPage(Collection<T> data, int pageSize, int pageNumber, int totalPages,
                                          long overAllAmountOfElements) {
        return new Page.Builder<T>()
                .setPageSize(pageSize)
                .setPageNumber(pageNumber)
                .setOverAllAmountOfElements(overAllAmountOfElements)
                .setTotalPages(totalPages)
                .setData(data)
                .build();
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

