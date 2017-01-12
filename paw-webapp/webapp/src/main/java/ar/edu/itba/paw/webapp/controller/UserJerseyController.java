package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.ProfilePictureDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserListDto;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * API endpoint for user management.
 */
@Path("users")
@Component
public class UserJerseyController {

    @Autowired
    private UserJerseyController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    private final UserService userService;

    private final SessionService sessionService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @GET
    //@PATH not needed, same as class annotation, and Jersey throws warning if included
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listUsers() {
        return Response.ok(new UserListDto(userService.all())).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
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
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteById(@PathParam("id") final long id) {
        //TODO remove ID parameter, get current user and only allow current user to delete their own account
        userService.deleteById(id);
        return Response.noContent().build();
    }

    /* ************************************
     *          PROFILE PICTURES
     * ***********************************/
    private static final List<String> SUPPORTED_PICTURE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif");

    @GET
    @Path("/{id}/picture")
    @Produces("image/*")
    public Response getProfilePicture(@PathParam("id") final long id) {
        final User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (user.hasProfilePicture()) {
            InputStream pictureStream = new BufferedInputStream(new ByteArrayInputStream(user.getProfilePicture()));
            String mimeType = user.getProfilePictureMimeType();
            return Response.ok(pictureStream, mimeType).build();
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

    @PUT
    @Path("/picture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(ProfilePictureDto picture) {
        long userId = sessionService.getCurrentUserId();
        if (userId == -1) {
            LOG.error("Couldn't get current user on profile picture PUT");
            return Response.serverError().build();
        }
        if(picture.getBase64Data() == null || picture.getBase64Data().isEmpty()) {
            LOG.info("No data for profile picture update for user #{}, returning HTTP 400 Bad Request", userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        byte[] pictureBytes;
        String mimeType;
        try {
            pictureBytes = java.util.Base64.getMimeDecoder().decode(picture.getBase64Data());
        } catch (IllegalArgumentException e) {  //Not Base64
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            mimeType = getMimeType(byteArrayToTempFile(pictureBytes));
            if (mimeType == null || !SUPPORTED_PICTURE_TYPES.contains(mimeType)) {
                return Response
                    .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                    .header("X-Supported-Media-Types", Arrays.toString(SUPPORTED_PICTURE_TYPES.toArray()).replace("[", "").replace("]", "").replace(" ", "")).build();
            }
        } catch (IOException | TikaException | SAXException e) {
            LOG.error("Error detecting MIME type of uploaded profile picture for user #{}: {}", userId, e);
            return Response.serverError().build();
        }
        userService.setProfilePicture(userId, pictureBytes, mimeType);
        LOG.info("Updated profile picture for user #{}", userId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/picture")
    @Produces("text/html")
    public Response deleteProfilePicture() {
        long userId = sessionService.getCurrentUserId();
        if (userId == -1) {
            LOG.error("Couldn't get current user on profile picture DELETE");
            return Response.serverError().build();
        }
        userService.removeProfilePicture(userId);
        LOG.info("Deleted profile picture for user #{}", userId);
        return Response.ok().build();
    }

    /**
     * Creates a temporary File from an input stream.
     *
     * @param is The data input stream.
     * @return The generated temporary file.
     * @throws IOException If an I/O error occurs.
     */
    private File inputStreamToTempFile(InputStream is) throws IOException {
        File tempFile = File.createTempFile("is2f", ".tmp");
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(is, out);
        return tempFile;
    }

    /**
     * Creates a temporary File from a byte array.
     *
     * @param data The data to convert to a file.
     * @return The temporary File.
     * @throws IOException If an I/O error occurs.
     * @see #inputStreamToTempFile(InputStream)
     */
    private File byteArrayToTempFile(byte[] data) throws IOException {
        return inputStreamToTempFile(new BufferedInputStream(new ByteArrayInputStream(data)));
    }

    /**
     * Browser-sent Content-Type headers of uploaded files can't always be trusted, so this method analyzes the file's
     * bytes to reliably get the data's MIME type.
     *
     * @param upload The uploaded file.
     * @return The detected MIME type, or {@code null} if not recognized.
     * @throws IOException If an I/O error occurs.
     */
    private String getMimeType(File upload) throws IOException, TikaException, SAXException {
        //TODO try to remove the overhead of creating a separate inputStream
        //Thanks http://stackoverflow.com/a/4583817/2333689
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<>());
        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, upload.getName());

        InputStream stream = new FileInputStream(upload);   //NOTE: Using the original input stream that the user uploaded here does NOT work
        parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        stream.close();

        String mimeType = metadata.get(HttpHeaders.CONTENT_TYPE);
        return mimeType;
    }
}
