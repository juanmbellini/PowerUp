package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.EntityNotPresentError;
import ar.edu.itba.paw.webapp.exceptions.InvalidReferencedEntityException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Will map into a 409 conflict response.
 * <p>
 * Created by Juan Marcos Bellini on 21/12/16.
 */
@Provider
public class EntityNotPresentExceptionMapper implements ExceptionMapper<InvalidReferencedEntityException> {

    @Override
    public Response toResponse(InvalidReferencedEntityException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorDto.ClientSideErrorDto
                        .EntityNotPresentErrorDto(new EntityNotPresentError(exception.getConflictingEntities())))
                .build();
    }
}
