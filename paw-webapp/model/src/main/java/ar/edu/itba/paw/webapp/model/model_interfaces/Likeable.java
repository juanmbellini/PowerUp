package ar.edu.itba.paw.webapp.model.model_interfaces;

/**
 * This interface does not implements any method, but it defines a type of entity that can be liked.
 */
public interface Likeable {

    /**
     * @return The entity's id.
     */
    long getId();

    /**
     * @return The amount of likes this entity has.
     */
    long getLikeCount();
}
