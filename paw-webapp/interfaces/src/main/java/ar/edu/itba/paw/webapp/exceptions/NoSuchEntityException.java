package ar.edu.itba.paw.webapp.exceptions;

import java.util.LinkedList;
import java.util.List;

/**
 * Exception thrown when an entity user was looked up by a primary key but no matching entity was found.
 */
public class NoSuchEntityException extends RuntimeException {


    /**
     * Stores the key names of the missing entities.
     */
    private List<String> missingEntitiesKeyNames = new LinkedList<>();

    public NoSuchEntityException(String message, List<String> missingEntities) {
        super(message);
        this.missingEntitiesKeyNames = missingEntities;
    }


    public NoSuchEntityException() {
        this("No such entity", new LinkedList<>());
    }

    public NoSuchEntityException(Class entity, Object primaryKey) {
        this("No " + entity.getSimpleName() + " found with primary key " + primaryKey, new LinkedList<>());
    }


    public NoSuchEntityException(List<String> missingEntitiesKeyNames) {
        this("", missingEntitiesKeyNames);
    }

    /**
     * Returns the list of missing entities' names.
     *
     * @return The list of missing entities names.
     */
    public List<String> getMissingEntitiesKeyNames() {
        return missingEntitiesKeyNames;
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityException(Throwable cause) {
        super(cause);
    }

    protected NoSuchEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
