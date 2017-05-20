package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.EntityDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;

import javax.ws.rs.PathParam;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // Check if id is positive
        if (id <= 0) {
            throw new IllegalParameterValueException(idFieldName);
        }
        if (dto == null) {
            throw new MissingJsonException();
        }
        // Check whether the id in the given dto is the same as the path id
        if (dto.getId() != null && id != dto.getId()) {
            throwValidationException(Stream
                    .of(new ValueError(ValueError.ErrorCause.ILLEGAL_VALUE,
                            "id", "Mismatch between object id and path id"))
                    .collect(Collectors.toList()));
        }
    }
}
