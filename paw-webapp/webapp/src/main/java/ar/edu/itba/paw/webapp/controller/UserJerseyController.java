package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.StringReader;
import java.util.Collections;


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

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listUsers() {
        return Response.ok(Collections.emptyList()).build();
    }

//    @POST
//    @Path("/")
//    @Produces(value = { MediaType.APPLICATION_JSON, })
//    public Response createUser(final UserDTO userDto) {
//        final User user = us.register(userDto.getUsername
//                (), userDto.getPassword());
//        final URI uri = uriInfo.getAbsolutePathBuilder()
//                .path(String.valueOf(user.getId()))
//                .build();
//        return Response.created(uri).build();
//    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getById(@PathParam("id") @DefaultValue("-1") final long id) {
        final User user = userService.findById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/test")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response test(String data) {
        JsonReader jsonReader = Json.createReader(new StringReader(data));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

//        if(!object.keySet().containsAll(Arrays.asList("email", "username", "password"))) {
//            return Response.status(422).build();
//        }
        String username = object.getString("username"),
                optional = object.getString("optional", null);
        int opt2 = object.getInt("int", -2);

        final User user = userService.findByUsername(object.getString("username", "lololol"));
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @DELETE
//    @Path("/{id}")
//    @Produces(value = { MediaType.APPLICATION_JSON, })
//    public Response deleteById(@PathParam("id") final long id) {
//        userService.deleteById(id);
//        return Response.noContent().build();
//    }
}
