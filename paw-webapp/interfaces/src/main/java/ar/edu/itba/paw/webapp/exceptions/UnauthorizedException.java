package ar.edu.itba.paw.webapp.exceptions;

/**
 * Exception thrown when users attempt to perform unauthorized actions, such as editing a comment that isn't theirs.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() { super(); }
    public UnauthorizedException(String msg) { super(msg); }
    public UnauthorizedException(Exception cause) { super(cause); }
}