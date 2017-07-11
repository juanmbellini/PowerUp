package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Authority;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ar.edu.itba.paw.webapp.controller.UserJerseyController.END_POINT;


/**
 * API endpoint for user management.
 */
@Path(END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class UserJerseyController implements UpdateParamsChecker {

    public static final String END_POINT = "users";

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


    // ================ API methods ================


    // ======== Basic user operation ========

    @GET
    public Response getUsers(@QueryParam("orderBy") @DefaultValue("username") final UserDao.SortingType sortingType,
                             @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                             @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                             @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                             // Filters
                             @QueryParam("username") @DefaultValue("") final String username,
                             @QueryParam("email") @DefaultValue("") final String email,
                             @QueryParam("authority") @DefaultValue("") final Authority authority) {
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        userService.getUsers(username, email, authority, pageNumber, pageSize, sortingType, sortDirection),
                        (userPage) -> new GenericEntity<List<UserDto>>(UserDto.createList(userPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("username", username)
                                .addParameter("email", email)
                                .addParameter("authority", authority)
                                .build());
    }


    @GET
    @Path("/{id : \\d+}")
    public Response getById(@PathParam("id") final long id) {
        if (id <= 0) {
            throw new IllegalParameterValueException("id");
        }
        final User user = userService.findById(id);
        //TODO borrar esto de agregar headers a mano
        // return Response.ok(new UserDto(user)).header("Access-Control-Allow-Origin", "*").header("Access-Control-Expose-Headers", "*").build();
        return user == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new UserDto(user)).build();
    }

    @GET
    @Path("/username/{username : .+}")
    public Response getByUsername(@PathParam("username") final String username) {
        if (username == null) {
            throw new IllegalParameterValueException("username");
        }
        final User user = userService.findByUsername(username);
        return user == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new UserDto(user)).build();
    }

    @GET
    @Path("/email/{email : .+}")
    public Response getByEMail(@PathParam("email") final String email) {
        if (email == null) {
            throw new IllegalParameterValueException("email");
        }
        final User user = userService.findByEmail(email);
        return user == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new UserDto(user)).build();
    }


    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createUser(final UserDto userDto) {
        if (userDto == null) {
            throw new MissingJsonException();
        }
        final User user = userService.create(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }


    @PUT
    @Path("/{id : \\d+}/password")
    public Response changePassword(@PathParam("id") final long userId,
                                   final UserDto userDto) {
        checkUpdateValues(userId, "id", userDto);
        userService.changePassword(userId, userDto.getPassword(), userId); // TODO: updater
        return Response.noContent().build();
    }


    // ======== Collections ========


    // ==== Play status ====

    @GET
    @Path("/{id : \\d+}/play-statuses")
    public Response getPlayStatuses(@PathParam("id") final long userId,
                                    // Pagination and Sorting
                                    @QueryParam("orderBy") @DefaultValue("game-id")
                                    final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                    @QueryParam("sortDirection") @DefaultValue("asc")
                                    final SortDirection sortDirection,
                                    @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                    @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                    // Filters
                                    @QueryParam("gameId") Long gameIdFilter,
                                    @QueryParam("gameName") String gameNameFilter) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        userService.getPlayStatuses(userId, gameIdFilter, gameNameFilter,
                                pageNumber, pageSize, sortingType, sortDirection),
                        (statusesPage) -> new GenericEntity<List<UserGameStatusDto>>(UserGameStatusDto
                                .createList(statusesPage.getData())) {
                        },
                        scoreAndStatusMap(gameIdFilter, gameNameFilter));

    }

    @POST
    @Path("/{id : \\d+}/play-statuses")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addPlayStatus(@PathParam("id") final long userId,
                                  final UserGameStatusDto userGameStatusDto) {
        checkUpdateValues(userId, "id", userGameStatusDto);
        userService.setPlayStatus(userId, userGameStatusDto.getGameId(), userGameStatusDto.getStatus(), userId); // TODO: updater
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userGameStatusDto.getGameId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id : \\d+}/play-statuses/{gameId : \\d+}")
    public Response removePlayStatus(@PathParam("id") final long userId,
                                     @PathParam("gameId") final long gameId) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        if (gameId <= 0) {
            throw new IllegalParameterValueException("gameId");
        }
        userService.removePlayStatus(userId, gameId, userId); // TODO: updater
        return Response.noContent().build();
    }


    // ==== Game scores ====


    @GET
    @Path("/{id : \\d+}/game-scores")
    public Response getGameScores(@PathParam("id") final long userId,
                                  // Pagination and Sorting
                                  @QueryParam("orderBy") @DefaultValue("game-id")
                                  final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                  @QueryParam("sortDirection") @DefaultValue("asc")
                                  final SortDirection sortDirection,
                                  @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                  // Filters
                                  @QueryParam("gameId") @DefaultValue("") Long gameIdFilter,
                                  @QueryParam("gameName") @DefaultValue("") String gameNameFilter) {
        //TODO: Allow sorting by user score
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        userService.getGameScores(userId, gameIdFilter, gameNameFilter,
                                pageNumber, pageSize, sortingType, sortDirection),
                        (scoresPage) -> new GenericEntity<List<UserGameScoreDto>>(UserGameScoreDto
                                .createList(scoresPage.getData())) {
                        },
                        scoreAndStatusMap(gameIdFilter, gameNameFilter));
    }

    @POST
    @Path("/{id : \\d+}/game-scores")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addGameScore(@PathParam("id") final long userId,
                                 final UserGameScoreDto userGameScoreDto) {
        checkUpdateValues(userId, "id", userGameScoreDto);
        userService.setGameScore(userId, userGameScoreDto.getGameId(), userGameScoreDto.getScore(), userId); // TODO: updater
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userGameScoreDto.getGameId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id : \\d+}/game-scores/{gameId : \\d+}")
    public Response removeGameScore(@PathParam("id") final long userId,
                                    @PathParam("gameId") final long gameId) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        if (gameId <= 0) {
            throw new IllegalParameterValueException("gameId");
        }
        userService.removeGameScore(userId, gameId, userId); // TODO: updater
        return Response.noContent().build();
    }


//    @DELETE
//    @Path("/{id}")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response deleteById(@PathParam("id") final long id) {
//        //TODO remove ID parameter, get current user and only allow current user to delete their own account
//        userService.deleteById(id);
//        return Response.noContent().build();
//    } TODO: redo this method


    // ================ Helper methods ================


    /**
     * Creates a map to be used in the
     * {@link #getGameScores(long, UserDao.PlayStatusAndGameScoresSortingType, SortDirection, int, int, Long, String)}
     * or the
     * {@link #getPlayStatuses(long, UserDao.PlayStatusAndGameScoresSortingType, SortDirection, int, int, Long, String)}
     * methods.
     *
     * @param gameIdFilter   Filter for game id.
     * @param gameNameFilter Filter for game name.
     * @return The resulting map.
     */
    private static Map<String, Object> scoreAndStatusMap(final Long gameIdFilter, final String gameNameFilter) {
        return JerseyControllerHelper.getParameterMapBuilder().clear()
                .addParameter("gameId", gameIdFilter)
                .addParameter("gameName", gameNameFilter)
                .build();
    }


    // ================ Un-migrated methods ================


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
            return Response.temporaryRedirect(URI.create(Game.DEFAULT_COVER_PICTURE_URL)).build();
        }
    }

    @OPTIONS
    @Path("/picture")
    public Response pictureOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                              //Required by CORS
                .header("Access-Control-Allow-Methods", "PUT,DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }

    @OPTIONS
    public Response userOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                              //Required by CORS
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }

    @PUT
    @Path("/picture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(String base64Input) {
        long userId = sessionService.getCurrentUserId();
        if (userId == -1) {
            LOG.error("Couldn't get current user on profile picture PUT");
            return Response.serverError().build();
        }
        if (base64Input == null || base64Input.isEmpty()) {
            LOG.info("No data for profile picture update for user #{}, returning HTTP 400 Bad Request", userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String[] inputData = base64Input.split(",");
        if (inputData.length != 2 || !inputData[0].matches("data:image/(\\w+);base64")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String providedMimeType = inputData[0].substring(5, inputData[0].indexOf(';'));
        String pictureBase64 = inputData[1];
        byte[] pictureBytes;
        String processedMimeType;
        try {
            //https://stackoverflow.com/a/43251650/2333689
            pictureBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(pictureBase64);
        } catch (IllegalArgumentException e) {  //Not Base64
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            processedMimeType = getMimeType(byteArrayToTempFile(pictureBytes));
            if (processedMimeType == null || !SUPPORTED_PICTURE_TYPES.contains(processedMimeType)) {
                return Response
                        .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .header("X-Supported-Media-Types", Arrays.toString(SUPPORTED_PICTURE_TYPES.toArray()).replace("[", "").replace("]", "").replace(" ", "")).build();
            }
        } catch (IOException | TikaException | SAXException e) {
            LOG.error("Error detecting MIME type of uploaded profile picture for user #{}: {}", userId, e);
            return Response.serverError().build();
        }
        if (!processedMimeType.equals(providedMimeType)) {
            LOG.warn("Received mismatching MIME type for profile picture update for {}, rejecting", sessionService.getCurrentUsername());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //Valid, update
        userService.changeProfilePicture(userId, pictureBytes, processedMimeType, userId);
        LOG.info("Updated profile picture for {}", sessionService.getCurrentUsername());
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
        userService.removeProfilePicture(userId, userId);   //The current user may only remove their own profile picture
        LOG.info("Deleted profile picture for {}", sessionService.getCurrentUsername());
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

    @GET
    @Path("/{userId : \\d+}/game-list")
    public Response listShelfGames(@PathParam("userId") final long userId,
                                   @QueryParam("orderBy") @DefaultValue("game-id")
                                   final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0));

        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        //TODO Add sorting and filtering to gameList
                        userService.getGameList(userId, pageNumber, pageSize, sortingType, sortDirection),
                        (gamesPage) -> new GenericEntity<List<UserGameStatusDto>>(UserGameStatusDto
                                .createList(gamesPage.getData())) {
                        },
                        scoreAndStatusMap(null,null));
    }
}
