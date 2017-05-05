package ar.edu.itba.paw.webapp.model.validation;

/**
 * Created by Juan Marcos Bellini on 12/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ValueError {

    /**
     * The error's code.
     */
    private ErrorCause errorCause;
    /**
     * The field whose value was incorrect's name.
     */
    private String fieldName;
    /**
     * The error message
     */
    private String errorMessage;

    public ValueError(ErrorCause errorCause, String fieldName, String errorMessage) {
        if (errorCause == null || fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.errorCause = errorCause;
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    /**
     * Error code getter.
     *
     * @return The error's code.
     */
    public ErrorCause getErrorCode() {
        return errorCause;
    }

    /**
     * Field name getter.
     *
     * @return The field's name.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Error message getter.
     *
     * @return The error's message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public enum ErrorCause {
        MISSING_VALUE, // For example, when something is required but the value is null.
        ILLEGAL_VALUE, // For example, when a name must be set, but the given string is empty.
        ALREADY_EXISTS // For example, when a new User has an already in use e-mail/username.
    }
}
