package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.ValidationError;
import ar.edu.itba.paw.webapp.exceptions.UniqueViolationException;
import ar.edu.itba.paw.webapp.missing_statuses.UnprocessableEntityStatus;
import ar.edu.itba.paw.webapp.model.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 28/12/16.
 */
@Provider
public class UniqueViolationExceptionMapper implements ExceptionMapper<UniqueViolationException> {
    @Override
    public Response toResponse(UniqueViolationException exception) {

        return Response.status(new UnprocessableEntityStatus())
                .entity(new ErrorDto.ClientSideErrorDto
                        .ValidationErrorDto(new ValidationError(exception.getUniqueFields().stream()
                        .map(each -> new ValidationException
                                .ValueError(ValidationException.ValueError.ErrorCause.ALREADY_EXISTS, each,
                                "The " + each + " field violates a unique constraint"))
                        .collect(Collectors.toList()))))
                .build();
    }
}
