package ar.edu.itba.paw.webapp.exceptions;

import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;

/**
 * This exception must be thrown when receiving an illegal path or query param value.
 * For example, if a numeric id must be positive, and a negative value is received, this exception must be thrown.
 * <p>
 * Created by Juan Marcos Bellini on 6/1/17.
 */
public class IllegalParameterValueException extends ParamException {

    public IllegalParameterValueException(Class<? extends Annotation> parameterType,
                                          String name, String defaultStringValue) {
        super(new IllegalArgumentException(), Response.Status.BAD_REQUEST, parameterType, name, defaultStringValue);
    }
}
