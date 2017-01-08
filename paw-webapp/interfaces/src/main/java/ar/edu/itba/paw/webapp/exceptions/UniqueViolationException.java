package ar.edu.itba.paw.webapp.exceptions;

import java.util.HashSet;
import java.util.Set;

/**
 * This exception must be thrown when creating (or updating) an object, resulting in an unique restriction violation.
 * For example, if user e-mails must be unique, and another user updates his/her e-mail into an already in use e-mail,
 * this exception is thrown.
 * <p>
 * Created by Juan Marcos Bellini on 28/12/16.
 */
public class UniqueViolationException extends RuntimeException {


    private static String MESSAGE = "One or more fields violates unique constraints";

    /**
     * Contains those fields that violated unique constraints.
     */
    private Set<String> uniqueFields;

    public UniqueViolationException() {
        this(MESSAGE);
    }

    public UniqueViolationException(String message) {
        this(message, new HashSet<>());
    }

    public UniqueViolationException(Set<String> uniqueFields) {
        this(MESSAGE, uniqueFields);
    }


    public UniqueViolationException(String message, Set<String> uniqueFields) {
        super(message);
        this.uniqueFields = uniqueFields;
    }

    public Set<String> getUniqueFields() {
        return uniqueFields;
    }
}
