package ar.edu.itba.paw.webapp.api_errors;

import java.util.Set;

/**
 * An illegal parameter value error is that one that occurs when sending illegal values in a query or path param.
 * For example, if an numeric id parameter must be positive, and receives a negative value, this error is returned.
 * <p>
 * This kind of error includes a collection of parameters that caused the error.
 * <p>
 * Created by Juan Marcos Bellini on 21/12/16.
 */
public class IllegalParameterValueError extends ClientSideError {

    private Set<String> conflictingParameters;

    public IllegalParameterValueError(Set<String> conflictingParameters) {
        super("Illegal Value Error", 2);
        this.conflictingParameters = conflictingParameters;
    }

    public Set<String> getConflictingParameters() {
        return conflictingParameters;
    }
}
