package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserListDto;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
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

    private Logger LOG = LoggerFactory.logger(getClass());

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
    @Path("/picture/{id}")
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
    @Path("/picture/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePicture(@PathParam("id") final long id,
                                         @FormDataParam("picture") final InputStream uploadStream) {
        if(!userService.existsWithId(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            String mimeType = getMimeType(uploadStream);
            if(mimeType == null || !SUPPORTED_PICTURE_TYPES.contains(mimeType)) {
                return Response
                        .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .header("X-Supported-Media-Types", Arrays.toString(SUPPORTED_PICTURE_TYPES.toArray()).replace("[", "").replace("]", "").replace(" ", "")).build();
            }
        } catch (IOException e) {
            LOG.error("Error detecting MIME type of uploaded profile picture for user #{}: {}", id, e);
            return Response.serverError().build();
        }
        byte[] picture;
        try {
            picture = IOUtils.toByteArray(uploadStream);
        } catch (IOException e) {
            LOG.error("Error saving uploaded profile picture for user #{}: {}", id, e);
            return Response.serverError().build();
        }
        userService.setProfilePicture(id, picture);
        return Response.noContent().build();
    }

    /**
     * Browser-sent Content-Type headers of uploaded files can't always be trusted, so this method analyzes the file's
     * bytes to reliably get the data's MIME type.
     *
     * @param upload InputStream of the uploaded file.
     * @return The detected MIME type, or {@code null} if not recognized.
     * @throws IOException If an I/O error occurs.
     */
    private String getMimeType(InputStream upload) throws IOException {
        //TODO try to remove the overhead of creating a file and a separate inputStream

        //1) Convert InputStream to temporary File
        final File tempFile = File.createTempFile("is2f", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(upload, out);
        }

        //Thanks http://stackoverflow.com/a/4583817/2333689
        //2) Set up Tika variables with the temp file
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<>());

        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, tempFile.getName());

        //3) Create a NEW input stream for the generated file and parse it (parsing the original stream doesn't work)
        InputStream stream = new FileInputStream(tempFile);
        try {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        } catch (SAXException | TikaException e) {
            e.printStackTrace();
        }
        stream.close();

        String mimeType = metadata.get(HttpHeaders.CONTENT_TYPE);
        return mimeType;
    }
}
