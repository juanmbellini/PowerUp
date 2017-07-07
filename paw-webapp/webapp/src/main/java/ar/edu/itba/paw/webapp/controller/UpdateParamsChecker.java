package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.EntityDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;

import java.util.Collections;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Interface implementing a default method to check parameters that must be included when updating a resource.
 * <p>
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
/* package */ interface UpdateParamsChecker extends ValidationExceptionThrower {


    /**
     * Check updating values.
     *
     * @param id          The id of the entity to be updated.
     * @param idFieldName The name the id field has.
     * @param dto         The dto containing the id.
     */
    default void checkUpdateValues(long id, String idFieldName, EntityDto dto) {
        if (dto == null) {
            throw new MissingJsonException();
        }
        checkUpdateValues(idFieldName, id, pathId -> pathId > 0,
                dto.getId(), (pathId, dtoId) -> dtoId == null || Long.compare(pathId, dtoId) == 0);
    }

    /**
     * Check updating values.
     *
     * @param idFieldName          The name of the id field.
     * @param pathId               The id taken from the url path.
     * @param idChecker            A {@link Predicate} that implements logic to check if the id is correct
     *                             (whose {@link Predicate#test(Object) method returns {@code true} if the id is correct,
     *                             or {@code false} otherwise}).
     * @param dtoField             The id taken from the dto.
     * @param idAndDtoFieldChecker A {@link BiPredicate} that implements logic to check if the dto and path id match
     *                             (whose {@link BiPredicate#test(Object, Object)} method returns {@code true}
     *                             if the ids match, or {@code false} otherwise}).
     * @param <T>                  The type of the ids object.
     * @throws IllegalParameterValueException If the path id is not correct.
     * @throws ValidationException            If the ids do not match.
     * @implNote The given {@code idChecker} must implement its {@link Predicate#test(Object) method in a way it
     * returns {@code true} if the id is correct, or {@code false} otherwise}. The given {@code idAndDtoFieldChecker}
     * must implement its {@link BiPredicate#test(Object, Object)} method in a way it returns {@code true}
     * if the ids match, or {@code false} otherwise}
     */
    default <T> void checkUpdateValues(String idFieldName, T pathId, Predicate<T> idChecker, T dtoField,
                                       BiPredicate<T, T> idAndDtoFieldChecker)
            throws IllegalParameterValueException, ValidationException {
        // Check id.
        if (idChecker.negate().test(pathId)) {
            throw new IllegalParameterValueException(idFieldName);
        }
        // Check whether the object id and the dto id match.
        if (dtoField != null && idAndDtoFieldChecker.negate().test(pathId, dtoField)) {
            throwValidationException(Collections.singletonList(new ValueError(ValueError.ErrorCause.ILLEGAL_VALUE,
                    idFieldName, "Mismatch between object id and path id")));
        }
    }
}
