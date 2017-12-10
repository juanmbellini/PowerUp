package ar.edu.itba.paw.webapp.exception_mappers;


import ar.edu.itba.paw.webapp.exceptions.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Will map into a 503 service unavailable response.
 * <p>
 * Created by Juan Marcos Bellini on 21/12/16.
 */
@Provider
public class ExternalServiceExceptionMapper implements ExceptionMapper<ExternalServiceException> {

    /**
     * A {@link Logger} to log any trouble when accessing the Twitch API.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ExternalServiceExceptionMapper.class);

    @Override
    public Response toResponse(ExternalServiceException exception) {
        LOGGER.warn("There was a problem accessing an external service. Exception message: {}", exception.getMessage());

        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
