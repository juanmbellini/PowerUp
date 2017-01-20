package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.model.ValidationException;

/**
 * This class represents an unique violation error.
 * It can be used to get an already configured error (with error cause and message).
 *
 * Created by Juan Marcos Bellini on 20/1/17.
 */
public class UniqueViolationError extends ValidationException.ValueError {
    public UniqueViolationError(String fieldName) {
        super(ErrorCause.ALREADY_EXISTS, fieldName, "The " + fieldName + " field violates a unique constraint");
    }


}
