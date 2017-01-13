package ar.edu.itba.paw.webapp.exception_mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.api_errors.RepresentationError;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// TODO: change to faster xml jackson provider to make this mapper work (other providers will use their own mappers)

/**
 * Created by Juan Marcos Bellini on 21/12/16.
 */
@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

	@Override
	public Response toResponse(JsonProcessingException exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorDto.ClientSideErrorDto
						.RepresentationErrorDto(new RepresentationError()))
				.build();
	}
}
