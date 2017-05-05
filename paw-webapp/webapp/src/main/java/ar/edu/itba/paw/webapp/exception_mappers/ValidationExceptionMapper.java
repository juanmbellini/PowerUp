package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.ValidationError;
import ar.edu.itba.paw.webapp.missing_statuses.UnprocessableEntityStatus;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Juan Marcos Bellini on 20/12/16.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {


    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(new UnprocessableEntityStatus())
                .entity(new ErrorDto.ClientSideErrorDto
                        .ValidationErrorDto(new ValidationError(exception.getErrors())))
                .build();
    }
}
