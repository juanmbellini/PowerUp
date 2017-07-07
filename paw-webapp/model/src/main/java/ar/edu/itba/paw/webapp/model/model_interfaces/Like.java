package ar.edu.itba.paw.webapp.model.model_interfaces;

import ar.edu.itba.paw.webapp.model.User;

/**
 * This class does not implement any method, but defines a type of entity that represents a like.
 */
public interface Like {

    /**
     * @return The {@link User} that created this like.
     */
    User getUser();
}
