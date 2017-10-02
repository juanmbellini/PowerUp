package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.api_errors.IllegalParameterValueError;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.exceptions.NumberOfPageBiggerThanTotalAmountException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Juan Marcos Bellini on 5/5/17.
 */
@Provider
public class NumberOfPageBiggerThanTotalAmountExceptionMapper
        implements ExceptionMapper<NumberOfPageBiggerThanTotalAmountException> {

    @Override
    public Response toResponse(NumberOfPageBiggerThanTotalAmountException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDto.ClientSideErrorDto
                        .IllegalParameterValueErrorDto(new IllegalParameterValueError(Stream.of("pageNumber")
                        .collect(Collectors.toSet()))))
                .build();
    }
}
