package ar.edu.itba.paw.webapp.exceptions;

/**
 * Exception thrown when an entity user was looked up by a primary key but no matching entity was found.
 */
public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
        super("No such entity");
    }

    public NoSuchEntityException(Class entity, Object primaryKey) {
        super("No " + entity.getSimpleName() + " found with primary key " + primaryKey);
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
