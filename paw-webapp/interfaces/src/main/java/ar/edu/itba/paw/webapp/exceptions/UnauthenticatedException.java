package ar.edu.itba.paw.webapp.exceptions;

/**
 * Exception thrown when a non authenticated user attempts to perform an action that requires authentication.
 */
public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(String msg) {
        super(msg);
    }

    public UnauthenticatedException(Exception cause) {
        super(cause);
    }
}
