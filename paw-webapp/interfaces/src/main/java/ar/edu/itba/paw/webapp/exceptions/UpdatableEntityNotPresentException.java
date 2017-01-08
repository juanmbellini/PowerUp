package ar.edu.itba.paw.webapp.exceptions;

/**
 * This exception is thrown when trying to update an object that does not exists.
 * <p>
 * Created by Juan Marcos Bellini on 6/1/17.
 */
public class UpdatableEntityNotPresentException extends RuntimeException {

    public UpdatableEntityNotPresentException() {
        super();
    }

    public UpdatableEntityNotPresentException(String message) {
        super(message);
    }
}
