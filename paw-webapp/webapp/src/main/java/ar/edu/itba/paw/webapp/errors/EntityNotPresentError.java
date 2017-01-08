package ar.edu.itba.paw.webapp.errors;

import java.util.Set;

/**
 * An entity not present error is that one that occurs when using ids of entities that does not exists.
 * For example, if an User is received, and it has a role that does not exist, this error is returned.
 * <p>
 * This kind of error includes a collection of fields that caused the error.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public class EntityNotPresentError extends ClientSideError {

    private Set<String> conflictingFields;

    public EntityNotPresentError(Set<String> conflictingFields) {
        super("Entities Not Present Error", 4);
        this.conflictingFields = conflictingFields;
    }

    public Set<String> getConflictingFields() {
        return conflictingFields;
    }


}
