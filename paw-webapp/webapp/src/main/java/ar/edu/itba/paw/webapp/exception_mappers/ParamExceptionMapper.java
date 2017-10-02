package ar.edu.itba.paw.webapp.exception_mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.InvalidParameterValueError;
import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 21/12/16.
 */
@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

	@Override
	public Response toResponse(ParamException exception) {
		Set<String> parameters = new HashSet<>();
		parameters.add(exception.getParameterName());
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorDto.ClientSideErrorDto
						.InvalidParameterValueErrorDto(new InvalidParameterValueError(parameters)))
				.build();
	}
}
