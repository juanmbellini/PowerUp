package ar.edu.itba.paw.webapp.exceptions;

/**
 * Exception thrown when attempting to create a user with an email or username that already exists.
 */
public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("User already exists");
    }

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistsException(Throwable cause) {
        super(cause);
    }

    protected UserExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
