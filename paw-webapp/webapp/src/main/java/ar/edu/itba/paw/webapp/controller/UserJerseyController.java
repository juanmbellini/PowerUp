package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private UserJerseyController(UserService userService, SessionService sessionService, MailService mailService, PasswordEncoder passwordEncoder, ShelfService shelfService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.shelfService = shelfService;
    }

    private final UserService userService;

    private final MailService mailService;

    private final ShelfService shelfService;

    private final SessionService sessionService;

    private PasswordEncoder passwordEncoder;

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
    @Path("/{id : \\d+}/password-change")
    public Response changePassword(@PathParam("id") final long userId,
                                   final UserDto userDto) {
        checkUpdateValues(userId, "id", userDto);
        String newPassword = passwordEncoder.encode(userDto.getPassword());
        userService.changePassword(userId, newPassword, userId); // TODO: updater
        mailService.sendEmailChangePassword(userService.findById(userId));
        return Response.noContent().build();
    }

    @POST
    @Path("/{id : \\d+}/password-reset")
    public Response resetPassword(@PathParam("id") final long userId) {
        String newPassword = userService.generateNewPassword();
        String hashedPassword = passwordEncoder.encode(newPassword);
        userService.changePassword(userId, hashedPassword, userId); // TODO: updater
//        mailService.sendEmailResetPassword(userService.findById(userId), newPassword);
        return Response.noContent().build();
    }


    // ======== Collections ========

    @GET
    @Path("/{userId : \\d+}/game-list")
    public Response listShelfGames(@PathParam("userId") final long userId,
                                   @QueryParam("orderBy") @DefaultValue("game-name")
                                   final UserDao.ListGameSortingType sortingType,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                   // Filters
                                   @QueryParam("shelfName") final List<String> shelfNames,
                                   @QueryParam("status") final List<PlayStatus> statuses) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0));
        final JerseyControllerHelper.ParameterMapBuilder parametersBuilder = JerseyControllerHelper
                .getParameterMapBuilder()
                .clear();
        shelfNames.forEach(name -> parametersBuilder.addParameter("shelfName", name));
        statuses.forEach(status -> parametersBuilder.addParameter("status", status));
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        //TODO Add sorting and filtering to gameList
                        userService.getGameList(userId, shelfNames, statuses,
                                pageNumber, pageSize, sortingType, sortDirection),
                        (gamesPage) -> new GenericEntity<List<ListGameDto>>(ListGameDto
                                .createList(gamesPage.getData(), uriInfo.getBaseUriBuilder())) {
                        }, parametersBuilder.build());

    }


    // ==== Play status ====

    @GET
    @Path("/{id : \\d+}/play-status/{gameId : \\d+}")
    public Response getPlayStatus(@PathParam("id") final long userId,
                                  @PathParam("gameId") Long gameIdFilter,
                                  // Pagination and Sorting
                                  @QueryParam("orderBy") @DefaultValue("game-id")
                                  final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                  @QueryParam("sortDirection") @DefaultValue("asc")
                                  final SortDirection sortDirection,
                                  @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
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

    @GET
    @Path("/play-statuses")
    public Response getPlayStatuses() {
        return Response.ok(new GenericEntity<List<PlayStatus>>(playStatusList()) {
        }).build();
    }

    private List<PlayStatus> playStatusList() {
        return Arrays.stream(PlayStatus.values()).collect(Collectors.toList());
    }

    @POST
    @Path("/{id : \\d+}/play-status")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addPlayStatus(@PathParam("id") final long userId,
                                  final UserGameStatusDto userGameStatusDto) {
        checkUpdateValues(userId, "id", userGameStatusDto);
        userService.setPlayStatus(userId, userGameStatusDto.getGameId(), userGameStatusDto.getStatus(), userId); // TODO: updater
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userGameStatusDto.getGameId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id : \\d+}/play-status/{gameId : \\d+}")
    public Response removePlayStatus(@PathParam("id") final long userId,
                                     @PathParam("gameId") final long gameId) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        if (gameId <= 0) {
            throw new IllegalParameterValueException("gameId");
        }
        userService.setPlayStatus(userId, gameId, PlayStatus.NO_PLAY_STATUS, userId); // TODO: updater
        if (!belongsToGameList(userId, gameId)) userService.removePlayStatus(userId, gameId, userId);
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
        if (userService.getPlayStatuses(userId, userGameScoreDto.getGameId(), null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData().isEmpty()) {
            userService.setPlayStatus(userId, userGameScoreDto.getGameId(), PlayStatus.NO_PLAY_STATUS, userId);
        }
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
        if (!belongsToGameList(userId, gameId)) deleteFromGameList(userId, gameId);
        return Response.noContent().build();
    }

    private void deleteFromGameList(long userId, long gameId) {
        userService.removePlayStatus(userId, gameId, userId);
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
     * {@link #getPlayStatus(long, Long, UserDao.PlayStatusAndGameScoresSortingType, SortDirection, int, int, String)}
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

    @OPTIONS
    @Path("/{id : \\d+}/play-status")
    public Response playStatusOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                              //Required by CORS
                .header("Access-Control-Allow-Methods", "PUT,DELETE,OPTIONS,POST,GET")
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }

    @OPTIONS
    @Path("/{id : \\d+}/game-scores")
    public Response scoreOptions() {
        return playStatusOptions();
    }

    @OPTIONS
    @Path("/{id : \\d+}/play-status/{gameId : \\d+}")
    public Response playStatusOptions2() {
        return playStatusOptions();
    }

    @OPTIONS
    @Path("/{id : \\d+}/game-scores/{gameId : \\d+}")
    public Response scoreOptions2() {
        return playStatusOptions();
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

    /* ========== Follow ========= */

    @GET
    @Path("/{id : \\d+}/users-following")
    public Response getUserFollowing(@PathParam("id") final long userId,
                                  // Pagination and Sorting
                                  @QueryParam("orderBy") @DefaultValue("id")
                                  final UserDao.SortingType sortingType,
                                  @QueryParam("sortDirection") @DefaultValue("asc")
                                  final SortDirection sortDirection,
                                  @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        userService.getUsersFollowing(userId, pageNumber, pageSize, sortingType, sortDirection),
                        (usersFollowingPage) -> new GenericEntity<List<UserDto>>(UserDto
                                .createList(usersFollowingPage.getData())) {
                        },
                        scoreAndStatusMap(null, null));
    }

    @GET
    @Path("/{id : \\d+}/users-followed-by")
    public Response getUserFollowedBy(@PathParam("id") final long userId,
                                     // Pagination and Sorting
                                     @QueryParam("orderBy") @DefaultValue("id")
                                     final UserDao.SortingType sortingType,
                                     @QueryParam("sortDirection") @DefaultValue("asc")
                                     final SortDirection sortDirection,
                                     @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                     @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        userService.getUserFollowedBy(userId, pageNumber, pageSize, sortingType, sortDirection),
                        (usersFollowingPage) -> new GenericEntity<List<UserDto>>(UserDto
                                .createList(usersFollowingPage.getData())) {
                        },
                        scoreAndStatusMap(null, null));
    }

    @PUT
    @Path("/{id : \\d+}/follow/")
    public Response followUser(@PathParam("id") final long followedId) {
        if (followedId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        userService.followUser(Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new), followedId);
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(followedId)).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id : \\d+}/unfollow/")
    public Response unFollowUser(@PathParam("id") final long followedId) {
        if (followedId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        userService.unFollowUser(Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new), followedId);
        return Response.noContent().build();
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
    @Path("/{id : \\d+}/recommended-games")
    public Response getRecommendedGames(@PathParam("id") final long userId,
                                        @QueryParam("shelves") final List<String> shelves) {
        if (userId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        Collection<Game> recommendedGames = userService.recommendGames(userId, shelves);
        return Response.ok(new GenericEntity<List<GameDto>>(GameDto.createList(recommendedGames)) {
        }).build();
    }


    /**
     * @return whether or not the game belongs to the User's GameList.
     */

    private boolean belongsToGameList(final long userId, final long gameId) {
        boolean hasPlayStatus = false;
        Collection<UserGameStatus> playStatuses = userService.getPlayStatuses(userId, gameId, null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData();
        if (playStatuses != null && playStatuses.iterator().hasNext()) {
            UserGameStatus ugs = playStatuses.iterator().next();
            if (ugs == null) {
                userService.setPlayStatus(userId, gameId, PlayStatus.NO_PLAY_STATUS, userId);
            } else {
                if (!ugs.getPlayStatus().equals(PlayStatus.NO_PLAY_STATUS)) hasPlayStatus = true;
            }
        }
        return !userService.getGameScores(userId, gameId, null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData().isEmpty()
                || !shelfService.getUserShelves(userId, null, gameId, null, 1, 1, ShelfDao.SortingType.ID, SortDirection.ASC).isEmpty()
                || hasPlayStatus;
    }
}
