package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
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

    private Logger LOG = LoggerFactory.getLogger(getClass());

    /*
     * NOTE: Login methods are not specified here since that is configured through Spring. See JsonAuthenticationFilter.
     */

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
     * @see #loginOptions()
     */
    @OPTIONS
    @Path("/logout")
    public Response logoutOptions() {
        Response.ResponseBuilder result = Response
            .ok()
            .allow("HEAD", "OPTIONS", "POST")
            .type(MediaType.TEXT_HTML)                                //Required by CORS
            .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }

    @GET
    @Path("/csrf")
    public Response getCsrfToken(CsrfToken token) {
        if(token != null) {
            LOG.warn("Attempted to get new CSRF token");    //TODO what to do in this case?
        }
        return Response.noContent().build();
    }
}
