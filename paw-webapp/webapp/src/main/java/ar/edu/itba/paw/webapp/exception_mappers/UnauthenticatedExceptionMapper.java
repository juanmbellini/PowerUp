package ar.edu.itba.paw.webapp.exception_mappers;

import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Juan Marcos Bellini on 20/5/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException> {

    @Override
    public Response toResponse(UnauthenticatedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(null).build();
    }
}
