package ar.edu.itba.paw.webapp.errors;

import java.util.Set;

/**
 * An invalid parameter value error is that one that occurs when sending invalid values in a query or path param.
 * For example, if an numeric id parameter receives a string, this error is returned.
 * <p>
 * This kind of error includes a collection of parameters that caused the error.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public class InvalidParameterValueError extends ClientSideError {

    private Set<String> conflictingParameters;

    public InvalidParameterValueError(Set<String> conflictingParameters) {
        super("Invalid Value Error", 1);
        this.conflictingParameters = conflictingParameters;
    }

    public Set<String> getConflictingParameters() {
        return conflictingParameters;
    }
}
