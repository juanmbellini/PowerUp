package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * DAO helper class. Provides package-private static methods useful to all DAOs.
 */
public class DaoHelper {

    @PersistenceContext
    //TODO this may not work; if it doesn't, implement singleton
    private static EntityManager entityManager;

    /**
     * Attempts to find an entity. If found, returns it, otherwise throws exception.
     *
     * @param klass The class of the entity to find
     * @param id The ID of the entity to find.
     * @return The found entity.
     * @throws NoSuchEntityException If no such entity exists.
     */
    /*package*/ static <T> T find(Class<T> klass, long id) {
        T result = entityManager.find(klass, id);
        if (result == null) {
            throw new NoSuchEntityException(klass, id);
        }
        return result;
    }

    /**
     * Performs a basic search with parameters, returning a single result.
     *
     * @param klass The class of the object type to return.
     * @param baseQuery The base HQL query.
     * @param parameters Parameters for the query, in order of appearance in the query.
     * @param <T> The return type.
     * @return The found entity, or {@code null} if not found.
     * @throws javax.persistence.NonUniqueResultException If more than one result is found.
     */
    /*package*/ static <T> T findSingleWithConditions(Class<T> klass, String baseQuery, Object ... parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        try {
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    /**
     * Performs a basic search with parameters, returning a list of results.
     *
     * @param klass The class of the object type to return.
     * @param baseQuery The base HQL query.
     * @param parameters Parameters for the query, in order of appearance in the query.
     * @param <T> The return type.
     * @return A list with the matching entities, or an empty list of nothing is found if not found.
     */
    public static <T> List<T> findManyWithConditions(Class<T> klass, String baseQuery, Object[] parameters) {
        TypedQuery<T> query = entityManager.createQuery(baseQuery, klass);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        try {
            return query.getResultList();
        } catch(NoResultException e) {
            return Collections.emptyList();
        }
    }
}
