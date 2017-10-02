package ar.edu.itba.paw.webapp.service;

import java.util.List;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;

/**
 * Interface declaring a default method that throws a {@link NoSuchEntityException} when reaching a certain coindition.
 * <p>
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public interface NoSuchEntityThrower {

    /**
     * Throws a {@link NoSuchEntityException} if there are missing entities (i.e the given list is not empty).
     *
     * @param missing The list containing missing entities (represented with the field name).
     */
    default void throwNoSuchEntityException(List<String> missing) {
        if (!missing.isEmpty()) {
            throw new NoSuchEntityException(missing);
        }
    }
}
