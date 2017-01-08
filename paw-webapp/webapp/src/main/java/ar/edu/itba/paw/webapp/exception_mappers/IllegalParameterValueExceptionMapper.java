package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.errors.IllegalParameterValueError;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 21/12/16.
 */
@Provider
public class IllegalParameterValueExceptionMapper implements ExceptionMapper<IllegalParameterValueException> {

	@Override
	public Response toResponse(IllegalParameterValueException exception) {
		Set<String> parameters = new HashSet<>();
		parameters.add(exception.getParameterName());
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorDto.ClientSideErrorDto
						.IllegalParameterValueErrorDto(new IllegalParameterValueError(parameters)))
				.build();
	}
}
