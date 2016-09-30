package ar.edu.itba.paw.webapp.exceptions;

/**
 * Created by julian on 28/09/16.
 */
public class FailedToProcessQueryException extends RuntimeException {

    public FailedToProcessQueryException() { super(); }
    public FailedToProcessQueryException(String msg) { super(msg); }
}
