package ar.edu.itba.paw.webapp.exception_mappers;

import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Juan Marcos Bellini on 20/5/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
