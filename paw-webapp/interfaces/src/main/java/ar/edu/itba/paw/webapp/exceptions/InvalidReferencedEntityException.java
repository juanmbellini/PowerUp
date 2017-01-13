package ar.edu.itba.paw.webapp.exceptions;

import java.util.HashSet;
import java.util.Set;

/**
 * This exception must be thrown when referencing an entity that is not present (for example, in the database).
 * <p>
 * Created by Juan Marcos Bellini on 21/12/16.
 */
public class InvalidReferencedEntityException extends RuntimeException {

    private static String MESSAGE = "One or more referenced entities were not present";

    /**
     * Contains those entities (or field names) that were not present.
     */
    private Set<String> conflictingEntities;

    public InvalidReferencedEntityException() {
        this(MESSAGE);
    }

    public InvalidReferencedEntityException(String message) {
        this(message, new HashSet<>());
    }

    public InvalidReferencedEntityException(Set<String> conflictingEntities) {
        this(MESSAGE, conflictingEntities);
    }


    public InvalidReferencedEntityException(String message, Set<String> conflictingEntities) {
        super(message);
        this.conflictingEntities = conflictingEntities;
    }

    public Set<String> getConflictingEntities() {
        return conflictingEntities;
    }
}
