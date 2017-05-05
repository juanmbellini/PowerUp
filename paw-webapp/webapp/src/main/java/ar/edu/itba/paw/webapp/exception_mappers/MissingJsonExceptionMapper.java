package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.RepresentationError;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Juan Marcos Bellini on 22/12/16.
 */
@Provider
public class MissingJsonExceptionMapper implements ExceptionMapper<MissingJsonException> {


	@Override
	public Response toResponse(MissingJsonException exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorDto.ClientSideErrorDto
						.RepresentationErrorDto(new RepresentationError()))
				.build();
	}
}
