package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.FindByIdDao;

import java.util.List;


/**
 * Interface declaring a default method that checks if a certaing object exists with a given id.
 * <p>
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * @param <T> The type of object to be queried for existence.
 */
public interface ExistenceByIdChecker<T> {


    /**
     * Checks whether the entity of type {@code T} exists with the given {@code id}.
     *
     * @param id        The id of the entity.
     * @param missing   The list containing already detected missing entities (represented with the field name).
     * @param fieldName The field name for this entity.
     * @param dao       The Data Access Object used for finding the entity.
     * @return The entity of type {@code T} with the given {@code id} if present, or {@code null} otherwise.
     */
    default T checkExistenceById(long id, List<String> missing, String fieldName, FindByIdDao<T> dao) {
        final T result = dao.findById(id);
        if (result == null) {
            missing.add(fieldName);
        }
        return result;
    }
}
