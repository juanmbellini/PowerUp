package ar.edu.itba.paw.webapp.model.model_interfaces;

/**
 * This interface defines methods for an entity that can be commented.
 */
public interface Commentable {

    /**
     * @return The entity's id.
     */
    long getId();

    /**
     * @return The amount of comments this entity has.
     */
    long getCommentCount();
}
