package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


/**
 * API endpoint for authentication.
 */
@Path("auth")
@Component
public class AuthController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private SessionService sessionService;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * An OPTIONS endpoint, required by CORS for local development, previous to actually POSTing to the login endpoint.
     *
     * @return A response with allowed HTTP methods and other headers required by CORS.
     */
    @OPTIONS
    @Path("/login")
    public Response loginOptions() {
        Response.ResponseBuilder result = Response
            .ok()
            .allow("HEAD", "OPTIONS", "POST")
            .type(MediaType.TEXT_HTML)                                //Required by CORS
            .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }

    /**
     * Log-in endpoint. If user is successfully authenticated, a JWT is generated and given to the user to use as
     * authentication for all future requests.
     */
    @POST
    @Path("/login")
    public Response login() {
        Response.ResponseBuilder result = Response
                .ok()
                .allow("HEAD", "OPTIONS", "POST")
                .type(MediaType.TEXT_HTML)                                //Required by CORS
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        //TODO send authenticated user data here?
        return result.build();
    }

    /**
     * @see #loginOptions()
     */
    @OPTIONS
    @Path("/logout")
    public Response logoutOptions() {
        Response.ResponseBuilder result = Response
            .ok()
            .allow("HEAD", "OPTIONS", "POST")
            .type(MediaType.TEXT_HTML)                                //Required by CORS
            .header("Access-Control-Allow-Headers", "Content-Type,Authorization");  //Required by CORS
        return result.build();
    }

    /**
     * Invalidates the authentication of the authenticated user. If the user wishes to access protected endpoints, they
     * must authenticate again.
     */
    @POST
    @Path("/logout")
    public Response logout() {
        LOG.info("{} logged out, JWT invalidated", sessionService.getCurrentUsername());
        return Response.noContent().build();
    }
}
