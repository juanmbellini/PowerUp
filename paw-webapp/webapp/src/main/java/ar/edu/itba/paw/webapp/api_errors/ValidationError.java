package ar.edu.itba.paw.webapp.api_errors;


import ar.edu.itba.paw.webapp.model.ValidationException;

import java.util.List;

/**
 * A validation error is that one that occurs when some json field does not validate.
 * For example, if a required field is not set, this kind of error is returned.
 * <p>
 * This kind of error includes a collection of sub-errors, containing all the information about each violation.
 * This information includes an error code
 * (see {@link ar.edu.itba.paw.webapp.model.ValidationException.ValueError.ErrorCause}), which field caused the error,
 * and an optional message.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public class ValidationError extends ClientSideError {

    private List<ValidationException.ValueError> errors;

    public ValidationError(List<ValidationException.ValueError> errors) {
        super("Validation Errors", 5);
        this.errors = errors;
    }

    public List<ValidationException.ValueError> getErrors() {
        return errors;
    }
}
