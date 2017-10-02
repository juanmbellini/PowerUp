package ar.edu.itba.paw.webapp.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This exception must be thrown when receiving an illegal path or query param value.
 * For example, if a numeric id must be positive, and a negative value is received, this exception must be thrown.
 * <p>
 * Created by Juan Marcos Bellini on 6/1/17.
 */
public class IllegalParameterValueException extends WebApplicationException/*ParamException*/ {

    /**
     * Contains those parameters what had wrong values.
     */
    private Set<String> wrongParameters = new HashSet<>();


    public IllegalParameterValueException(Set<String> wrongParameters) {
        super(new IllegalArgumentException(), Response.Status.BAD_REQUEST);
        this.wrongParameters = wrongParameters;
    }

    public IllegalParameterValueException(String parameter) {
        this(Stream.of(parameter).collect(Collectors.toSet()));
    }


    public Set<String> getWrongParameters() {
        return wrongParameters;
    }
}
