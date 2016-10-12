package ar.edu.itba.paw.webapp.exceptions;

/**
 * Created by Juan Marcos Bellini on 12/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class IllegalPageException extends RuntimeException {

    public IllegalPageException() { super(); }
    public IllegalPageException(String msg) { super(msg); }
}
