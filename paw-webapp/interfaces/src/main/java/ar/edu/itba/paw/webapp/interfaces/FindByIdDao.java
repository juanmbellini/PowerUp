package ar.edu.itba.paw.webapp.interfaces;

/**
 * Interface declaring a method to find an object by its id.
 * <p>
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * @param <T> The object type to be returned by the method.
 */

public interface FindByIdDao<T> {

    /**
     * Finds the object of type {@code T} that has the given {@code id}.
     *
     * @param id The objects id.
     * @return The object.
     */
    T findById(long id);
}
