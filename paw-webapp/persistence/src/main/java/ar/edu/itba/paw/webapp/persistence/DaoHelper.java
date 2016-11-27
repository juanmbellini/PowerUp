package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
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
     * @return              The found entity, or {@code null} if not found.
     */
    /*package*/ static <T> T findSingle(EntityManager entityManager, Class<T> klass, long id) {
        return entityManager.find(klass, id);
    }

    /**
     * Attempts to find an entity by ID. If found, returns it, otherwise throws exception.
     *
     * @param entityManager An entity manager with which to perform queries.
     * @param klass         The class of the entity to find.
     * @param id            The ID of the entity to find.
     * @return              The found entity.
     * @throws NoSuchEntityException If no such entity exists.
     */
    /*package*/ static <T> T findSingleOrThrow(EntityManager entityManager, Class<T> klass, long id) throws NoSuchEntityException {
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
    /*package*/ static <T> T findSingleWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery, Object... parameters) throws NonUniqueResultException {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i+1, parameters[i]);
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
     * @return              The found entities.
     */
    /*package*/ static <T> List<T> findAll(EntityManager entityManager, Class<T> klass) {
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
     * @return              A list with the matching entities, or an empty list of nothing is found if not found.
     */
    /*package*/ static <T> List<T> findManyWithConditions(EntityManager entityManager, Class<T> klass, String baseQuery, Object... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i+1, parameters[i]);
        }
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
