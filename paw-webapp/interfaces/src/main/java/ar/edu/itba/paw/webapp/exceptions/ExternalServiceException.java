package ar.edu.itba.paw.webapp.exceptions;

/**
 * Exception to be thrown in case there is any trouble when communicating with external services.
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException() {
        super();
    }

    public ExternalServiceException(String msg) {
        super(msg);
    }

    public ExternalServiceException(Throwable cause) {
        super(cause);
    }

    public ExternalServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
