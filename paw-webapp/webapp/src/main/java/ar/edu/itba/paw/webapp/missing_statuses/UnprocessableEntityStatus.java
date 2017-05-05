package ar.edu.itba.paw.webapp.missing_statuses;

import javax.ws.rs.core.Response;

/**
 * 422 HTTP Status.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public class UnprocessableEntityStatus implements Response.StatusType {
    @Override
    public int getStatusCode() {
        return 422;
    }

    @Override
    public Response.Status.Family getFamily() {
        return Response.Status.Family.CLIENT_ERROR;
    }

    @Override
    public String getReasonPhrase() {
        return "Unprocessable Entity";
    }
}
