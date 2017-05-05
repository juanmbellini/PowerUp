package ar.edu.itba.paw.webapp.exception_mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

// TODO: not in use now, but change it to some response when deploying

/**
 * Created by Juan Marcos Bellini on 21/12/16.
 */
//@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        return Response.serverError().entity(exception.getMessage()).build();
    }
}
