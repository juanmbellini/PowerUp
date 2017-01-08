package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.exceptions.UpdatableEntityNotPresentException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Juan Marcos Bellini on 22/12/16.
 */
@Provider
public class UpdatableEntityNotPresentExceptionMapper implements ExceptionMapper<UpdatableEntityNotPresentException> {
    @Override
    public Response toResponse(UpdatableEntityNotPresentException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
