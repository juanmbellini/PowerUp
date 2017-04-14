package ar.edu.itba.paw.webapp.model.validation;

import java.util.LinkedList;
import java.util.List;

/**
 * This Exception must be thrown when there are model validation errors.
 * These errors include nulls for required fields, out of range values, etc.
 * <p>
 * Created by Juan Marcos Bellini on 7/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ValidationException extends RuntimeException {

    private List<ValueError> errors;

    public ValidationException() {
        super();
        errors = new LinkedList<>();
    }

    public ValidationException(String message) {
        super(message);
        this.errors = new LinkedList<>();
    }

    public ValidationException(String message, List<ValueError> errors) {
        super(message);
        if (errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.errors = errors;
    }

    public ValidationException(List<ValueError> errors) {
        super();
        if (errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.errors = errors;
    }


    public List<ValueError> getErrors() {
        return errors;
    }

}
