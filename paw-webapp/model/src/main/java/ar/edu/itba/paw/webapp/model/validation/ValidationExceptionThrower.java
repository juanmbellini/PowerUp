package ar.edu.itba.paw.webapp.model.validation;

import java.util.List;

/**
 * Interface implementing a {@code default} method to throw ValidationExceptions.
 * <p>
 * Created by Juan Marcos Bellini on 13/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public interface ValidationExceptionThrower {

    /**
     * Throws a {@link ValidationException} if the given {@code errorList} is not empty.
     *
     * @param errorList The error list containing errors.
     * @throws ValidationException If the given {@code errorList} is not empty.
     */
    default void throwValidationException(List<ValueError> errorList) throws ValidationException {
        if (!errorList.isEmpty()) {
            throw new ValidationException(errorList);
        }
    }
}
