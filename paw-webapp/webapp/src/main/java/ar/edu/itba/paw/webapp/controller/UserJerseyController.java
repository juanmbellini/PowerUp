package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserListDto;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
import java.net.URLConnection;
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
    private UserJerseyController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG =  LoggerFactory.getLogger(getClass());

    @GET
    //@PATH not needed, same as class annotation, and Jersey throws warning if included
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listUsers() {
        return Response.ok(new UserListDto(userService.all())).build();
    }

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
    private static final List<String> SUPPORTED_PICTURE_TYPES = Arrays.asList("image/jpeg","image/jpg","image/png","image/gif");

    @GET
    @Path("/{id}/picture")
    @Produces("image/*")
    public Response getProfilePicture(@PathParam("id") final long id) {
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

    @PUT
    @Path("/{id}/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePicture(@PathParam("id") final long id,
                                         @FormDataParam("picture") final InputStream uploadStream) {
        if(!userService.existsWithId(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        File picture;
        try {
            picture = inputStreamToTempFile(uploadStream);
            String mimeType = getMimeType(picture);
            if(mimeType == null || !SUPPORTED_PICTURE_TYPES.contains(mimeType)) {
                return Response
                        .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .header("X-Supported-Media-Types", Arrays.toString(SUPPORTED_PICTURE_TYPES.toArray()).replace("[", "").replace("]", "").replace(" ", "")).build();
            }
        } catch (IOException | TikaException | SAXException e) {
            LOG.error("Error detecting MIME type of uploaded profile picture for user #{}: {}", id, e);
            return Response.serverError().build();
        }
        byte[] pictureBytes;
        try {
            pictureBytes = FileUtils.readFileToByteArray(picture);
        } catch (IOException e) {
            LOG.error("Error saving uploaded profile picture for user #{}: {}", id, e);
            return Response.serverError().build();
        }
        userService.setProfilePicture(id, pictureBytes);
        LOG.info("Updated profile picture for user #{}", id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}/picture")
    @Produces("text/html")
    public Response deleteProfilePicture(@PathParam("id") final long id) {
        if(!userService.existsWithId(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userService.removeProfilePicture(id);
        LOG.info("Deleted profile picture for user #{}", id);
        return Response.ok().build();
    }

    /**
     * Creates a temporary file from an input stream.
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
