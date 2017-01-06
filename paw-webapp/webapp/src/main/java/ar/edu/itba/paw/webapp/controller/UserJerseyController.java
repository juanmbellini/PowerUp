package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserListDto;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;


/**
 * API endpoint for user management.
 */
@Path("users")
@Component
public class UserJerseyController {

    @Autowired
    private UserJerseyController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG = LoggerFactory.logger(getClass());

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listUsers() {
        return Response.ok(new UserListDto(userService.all())).build();
    }

//    @POST
//    @Path("/")
//    @Produces(value = { MediaType.APPLICATION_JSON, })
//    public Response createUser(final UserDto userDto) {
//        final User user = userService.register(userDto.getUsername
//                (), userDto.getPassword());
//        final URI uri = uriInfo.getAbsolutePathBuilder()
//                .path(String.valueOf(user.getId()))
//                .build();
//        return Response.created(uri).build();
//    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getById(@PathParam("id") final long id) {
        final User user = userService.findById(id);
        if (user != null) {
            return Response.ok(new UserDto(user)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response deleteById(@PathParam("id") final long id) {
        //TODO remove ID parameter, get current user and only allow current user to delete their own account
        userService.deleteById(id);
        return Response.noContent().build();
    }

    /* ************************************
     *          PROFILE PICTURES
     * ***********************************/

    @GET
    @Path("/picture/{id}")
    @Produces("image/*")
    public Response profile(@PathParam("id") final long id) {
        final User user = userService.findById(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(user.hasProfilePicture()) {
            //Get image type
            //TODO stop guessing content type, add an extra column in the DB saving the profile picture's MIME type and read it from there
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(user.getProfilePicture()));
            try {
                String mimeType = URLConnection.guessContentTypeFromStream(is);
                return Response.ok(is, mimeType).build();
            } catch (IOException e) {
                LOG.error("Error serving profile picture for user #{}: {}", id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            //Serve default profile picture
            try {
                return Response.temporaryRedirect(new URI(Game.DEFAULT_COVER_PICTURE_URL)).build();
            } catch (URISyntaxException e) {
                LOG.error("Error serving default profile picture for user #{}: {}", id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
