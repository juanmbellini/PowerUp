package ar.edu.itba.paw.webapp.exceptions;

/**
 * Created by Julian on 10/30/2016.
 */
public class notImplementedException extends RuntimeException {
    public notImplementedException() {
        super("Method not yet implemented");
    }

    public notImplementedException(String message) {
        super(message);
    }

    public notImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public notImplementedException(Throwable cause) {
        super(cause);
    }

    protected notImplementedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}