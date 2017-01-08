package ar.edu.itba.paw.webapp.exceptions;

/**
 * This exception must be thrown when nothing is included in a request that requires some data (JSON/XML) in its body.
 * <p>
 * Created by Juan Marcos Bellini on 6/1/17.
 */
public class MissingJsonException extends RuntimeException {

    public MissingJsonException() {
        super();
    }

    public MissingJsonException(String message) {
        super(message);
    }
}
