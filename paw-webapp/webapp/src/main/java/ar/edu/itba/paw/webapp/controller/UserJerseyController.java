package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model_wrappers.CommentableAndLikeableWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;
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
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.controller.UserJerseyController.END_POINT;


/**
 * API endpoint for user management.
 */
@Path(END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class UserJerseyController implements UpdateParamsChecker {

    public static final String END_POINT = "users";

    public static final String FOLLOWING_END_POINT = "following";

    public static final String FOLLOWERS_END_POINT = "followers";

    public static final String FEED_END_POINT = "feed";

    public static final String PICTURE_END_POINT = "picture";

    @Autowired
    private UserJerseyController(UserService userService, SessionService sessionService,
                                 MailService mailService, ShelfService shelfService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.mailService = mailService;
        this.shelfService = shelfService;
    }

    private final UserService userService;

    private final MailService mailService;

    private final ShelfService shelfService;

    private final SessionService sessionService;


    @Context
    private UriInfo uriInfo;

    private final static Logger LOG = LoggerFactory.getLogger(UserJerseyController.class);


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
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber));

        final Page<UserWithFollowCountsWrapper> users =
                userService.getUsers(username, email, authority, pageNumber, pageSize, sortingType, sortDirection,
                        sessionService.getCurrentUser());
        final Map<String, Object> paramsForPaginationMap = JerseyControllerHelper.getParameterMapBuilder().clear()
                .addParameter("username", username)
                .addParameter("email", email)
                .addParameter("authority", authority)
                .build();
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection, users,
                        (userPage) -> new GenericEntity<List<UserDto>>(UserDto.createList(userPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        }, paramsForPaginationMap);
    }


    @GET
    @Path("/{id : \\d+}")
    public Response getById(@PathParam("id") final long id) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("id", id, idLambda -> idLambda <= 0));

        return Optional.ofNullable(userService.findById(id, sessionService.getCurrentUser()))
                .map(wrapper -> Response.ok(new UserDto(wrapper, uriInfo.getBaseUriBuilder())))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/username/{username : .+}")
    public Response getByUsername(@PathParam("username") final String username) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("username", username, Objects::isNull));

        return Optional.ofNullable(userService.findByUsername(username, sessionService.getCurrentUser()))
                .map(wrapper -> Response.ok(new UserDto(wrapper, uriInfo.getBaseUriBuilder())))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/email/{email : .+}")
    public Response getByEMail(@PathParam("email") final String email) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("email", email, Objects::isNull));

        return Optional.ofNullable(userService.findByEmail(email, sessionService.getCurrentUser()))
                .map(wrapper -> Response.ok(new UserDto(wrapper, uriInfo.getBaseUriBuilder())))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
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
    @Path("/password")
    public Response changePassword(final UserDto userDto) {
        userService.changePassword(sessionService.getCurrentUserId(), userDto.getPassword(),
                sessionService.getCurrentUserId());
        mailService.sendPasswordChangedEmail(sessionService.getCurrentUser());
        return Response.noContent().build();
    }

    @OPTIONS
    @Path("/password")
    public Response passwordChangeOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                             //Required by CORS
                .header("Access-Control-Allow-Methods", "PUT")
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }


    // TODO: these must be changed...

    @DELETE
    @Path("/{id : \\d+}/password")
    public Response resetPassword(@PathParam("id") final long userId) {
//        String newPassword = userService.generateNewPassword();
//        userService.changePassword(userId, newPassword, userId);
//        mailService.sendPasswordResetEmail(userService.findById(userId).getUser(), newPassword);
        return Response.noContent().build();
    }

    @OPTIONS
    @Path("/{id : \\d+}/password")
    public Response passwordResetOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                             //Required by CORS
                .header("Access-Control-Allow-Methods", "DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type");  //Required by CORS
        return result.build();
    }


    // ======== Collections ========

    @GET
    @Path("/{userId : \\d+}/game-list")
    public Response listShelfGames(@PathParam("userId") final long userId,
                                   @QueryParam("orderBy") @DefaultValue("game-name") final UserDao.ListGameSortingType sortingType,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                   // Filters
                                   @QueryParam("shelfName") final List<String> shelfNames,
                                   @QueryParam("status") final List<PlayStatus> statuses) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0));

        final Page<GameWithUserShelvesWrapper> gameList = userService.getGameList(userId, shelfNames, statuses,
                pageNumber, pageSize, sortingType, sortDirection);
        final JerseyControllerHelper.ParameterMapBuilder parametersBuilder = JerseyControllerHelper
                .getParameterMapBuilder()
                .clear();
        shelfNames.forEach(name -> parametersBuilder.addParameter("shelfName", name));
        statuses.forEach(status -> parametersBuilder.addParameter("status", status));
        final Map<String, Object> paramsForPaginationMap = parametersBuilder.build();
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection, gameList,
                        (gamesPage) -> new GenericEntity<List<ListGameDto>>(ListGameDto.createList(gamesPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        }, paramsForPaginationMap);

    }

    // ==== Follow ====

    @GET
    @Path("/{id : \\d+}/" + FOLLOWING_END_POINT)
    public Response getUserFollowing(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long userId,
                                     // Pagination and Sorting
                                     @QueryParam("sortDirection") @DefaultValue("asc") final SortDirection sortDirection,
                                     @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                     @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final Page<User> following = userService.getFollowing(userId, pageNumber, pageSize, sortDirection);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, "", sortDirection, following,
                        (usersFollowingPage) -> new GenericEntity<List<UserDto>>(UserDto
                                .createListWithoutFollowCount(usersFollowingPage.getData(),
                                        uriInfo.getBaseUriBuilder())) {
                        });
    }

    @GET
    @Path("/{id : \\d+}/" + FOLLOWERS_END_POINT)
    public Response getUserFollowedBy(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long userId,
                                      // Pagination and Sorting
                                      @QueryParam("sortDirection") @DefaultValue("asc") final SortDirection sortDirection,
                                      @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                      @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final Page<User> followers = userService.getFollowers(userId, pageNumber, pageSize, sortDirection);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, "", sortDirection, followers,
                        (usersFollowingPage) -> new GenericEntity<List<UserDto>>(UserDto
                                .createListWithoutFollowCount(usersFollowingPage.getData(),
                                        uriInfo.getBaseUriBuilder())) {
                        });
    }

    @PUT
    @Path("/{id : \\d+}/" + FOLLOWERS_END_POINT)
    public Response followUser(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long followedId) {
        if (followedId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        userService.followUser(followedId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id : \\d+}/" + FOLLOWERS_END_POINT)
    public Response unFollowUser(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long followedId) {
        if (followedId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        userService.unFollowUser(followedId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }


    // ==== Feed ====

    @GET
    @Path("/{id : \\d+}/" + FEED_END_POINT + "/" + ThreadJerseyController.END_POINT)
    public Response getFeedThreads(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long userId,
                                   // Pagination
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final User user = Optional.ofNullable(sessionService.getCurrentUser())
                .orElseThrow(UnauthenticatedException::new);
        final Page<CommentableAndLikeableWrapper<Thread>> threads =
                userService.getThreadsForFeed(user, pageNumber, pageSize);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, "", null, threads,
                        (page) -> new GenericEntity<List<ThreadDto>>(ThreadDto.createList(page.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        });
    }

    @GET
    @Path("/{id : \\d+}/" + FEED_END_POINT + "/" + ReviewJerseyController.END_POINT)
    public Response getFeedReviews(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long userId,
                                   // Pagination
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final User user = Optional.ofNullable(sessionService.getCurrentUser())
                .orElseThrow(UnauthenticatedException::new);
        final Page<Review> reviews = userService.getReviewsForFeed(user, pageNumber, pageSize);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, "", null, reviews,
                        (page) -> new GenericEntity<List<ReviewDto>>(ReviewDto.createListWithoutCount(page.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        });
    }


    @GET
    @Path("/{id : \\d+}/" + FEED_END_POINT + "/" + "statuses")
    public Response getFeedPlayStatuses(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long userId,
                                        // Pagination
                                        @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                        @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final User user = Optional.ofNullable(sessionService.getCurrentUser())
                .orElseThrow(UnauthenticatedException::new);
        final Page<UserGameStatus> gameStatuses = userService.getPlayStatusesForFeed(user, pageNumber, pageSize);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, "", null, gameStatuses,
                        (page) -> new GenericEntity<List<UserGameStatusDto>>(UserGameStatusDto
                                .createList(page.getData(), uriInfo.getBaseUriBuilder())) {
                        });
    }


    // ==== Play status ====

    @GET
    @Path("/{id : \\d+}/play-status/{gameId : \\d+}")
    public Response getPlayStatus(@PathParam("id") final long userId,
                                  @PathParam("gameId") Long gameIdFilter,
                                  // Pagination and Sorting
                                  @QueryParam("orderBy") @DefaultValue("game-id") final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                  @QueryParam("sortDirection") @DefaultValue("asc") final SortDirection sortDirection,
                                  @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                  @QueryParam("gameName") String gameNameFilter) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final Page<UserGameStatus> gameStatuses = userService.getPlayStatuses(userId, gameIdFilter, gameNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<String, Object> paramsForPaginationMap = scoreAndStatusMap(gameIdFilter, gameNameFilter);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection, gameStatuses,
                        (page) -> new GenericEntity<List<UserGameStatusDto>>(UserGameStatusDto
                                .createList(page.getData(), uriInfo.getBaseUriBuilder())) {
                        }, paramsForPaginationMap);
    }

    @GET
    @Path("/play-statuses")
    public Response getPlayStatuses() {
        final List<PlayStatus> statusesList = Arrays.stream(PlayStatus.values()).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<PlayStatus>>(statusesList) {
        }).build();
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
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("id", userId, id -> id <= 0)
                .addParameter("gameId", gameId, id -> id <= 0));

        userService.setPlayStatus(userId, gameId, PlayStatus.NO_PLAY_STATUS, userId); // TODO: updater
        // TODO: move this logic to service
        if (!belongsToGameList(userId, gameId)) userService.removePlayStatus(userId, gameId, userId);
        return Response.noContent().build();
    }


    // ==== Game scores ====


    @GET
    @Path("/{id : \\d+}/game-scores")
    public Response getGameScores(@PathParam("id") final long userId,
                                  // Pagination and Sorting
                                  @QueryParam("orderBy") @DefaultValue("game-id") final UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                  @QueryParam("sortDirection") @DefaultValue("asc") final SortDirection sortDirection,
                                  @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                  // Filters
                                  @QueryParam("gameId") @DefaultValue("") Long gameIdFilter,
                                  @QueryParam("gameName") @DefaultValue("") String gameNameFilter) {
        // TODO: Allow sorting by user score
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("id", userId, id -> id <= 0));

        final Page<UserGameScore> scores = userService.getGameScores(userId, gameIdFilter, gameNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<String, Object> paramsForPaginationMap = scoreAndStatusMap(gameIdFilter, gameNameFilter);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection, scores,
                        (scoresPage) -> new GenericEntity<List<UserGameScoreDto>>(UserGameScoreDto
                                .createList(scoresPage.getData())) {
                        }, paramsForPaginationMap);
    }

    @POST
    @Path("/{id : \\d+}/game-scores")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addGameScore(@PathParam("id") final long userId,
                                 final UserGameScoreDto userGameScoreDto) {
        checkUpdateValues(userId, "id", userGameScoreDto);

        userService.setGameScore(userId, userGameScoreDto.getGameId(), userGameScoreDto.getScore(), userId); // TODO: updater
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userGameScoreDto.getGameId())).build();

        // TODO: move this logic to service
        if (userService.getPlayStatuses(userId, userGameScoreDto.getGameId(), null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData().isEmpty()) {
            userService.setPlayStatus(userId, userGameScoreDto.getGameId(), PlayStatus.NO_PLAY_STATUS, userId);
        }
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id : \\d+}/game-scores/{gameId : \\d+}")
    public Response removeGameScore(@PathParam("id") final long userId,
                                    @PathParam("gameId") final long gameId) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("id", userId, id -> id <= 0)
                .addParameter("gameId", gameId, id -> id <= 0));

        userService.removeGameScore(userId, gameId, userId); // TODO: updater
        // TODO: move this logic to service
        if (!belongsToGameList(userId, gameId)) deleteFromGameList(userId, gameId);
        return Response.noContent().build();
    }

    private void deleteFromGameList(long userId, long gameId) {
        userService.removePlayStatus(userId, gameId, userId);
    }


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
    @Path("/{id : \\d+}/" + PICTURE_END_POINT)
    @Produces("image/*")
    public Response getProfilePicture(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id) {
        return Optional.ofNullable(userService.findById(id))
                .map(UserWithFollowCountsWrapper::getUser)
                .filter(User::hasProfilePicture)
                .map(user -> {
                    final InputStream byteArrayStream = new ByteArrayInputStream(user.getProfilePicture());
                    final InputStream imageStream = new BufferedInputStream(byteArrayStream);
                    final String mimeType = user.getProfilePictureMimeType();

                    return Response.ok(imageStream, mimeType);
                })
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @OPTIONS
    @Path("/" + PICTURE_END_POINT)
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

    @OPTIONS
    @Path("/{id : \\d+}/" + FOLLOWERS_END_POINT)
    public Response followOptions() {
        return playStatusOptions();
    }

    @PUT
    @Path("/" + PICTURE_END_POINT)
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
            return Response.status(Response.Status.BAD_REQUEST).build(); // TODO: This should be managed by error system
        }

        String providedMimeType = inputData[0].substring(5, inputData[0].indexOf(';'));
        String pictureBase64 = inputData[1];
        byte[] pictureBytes;
        String processedMimeType;
        try {
            //https://stackoverflow.com/a/43251650/2333689
            pictureBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(pictureBase64);
        } catch (IllegalArgumentException e) {  // Not Base64
            return Response.status(Response.Status.BAD_REQUEST).build(); // TODO: This should be managed by error system
        }

        try {
            processedMimeType = getMimeType(byteArrayToTempFile(pictureBytes));
            if (processedMimeType == null || !SUPPORTED_PICTURE_TYPES.contains(processedMimeType)) {
                return Response
                        .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .header("X-Supported-Media-Types",
                                Arrays.toString(SUPPORTED_PICTURE_TYPES.toArray())
                                        .replace("[", "")
                                        .replace("]", "")
                                        .replace(" ", ""))
                        .build(); // TODO: This should be managed by error system
            }
        } catch (IOException | TikaException | SAXException e) {
            LOG.error("Error detecting MIME type of uploaded profile picture for user #{}: {}", userId, e);
            return Response.serverError().build(); // TODO: This should be managed by error system
        }
        if (!processedMimeType.equals(providedMimeType)) {
            LOG.warn("Received mismatching MIME type for profile picture update for {}, rejecting",
                    sessionService.getCurrentUsername());
            return Response.status(Response.Status.BAD_REQUEST).build(); // TODO: This should be managed by error system
        }
        //Valid, update
        userService.changeProfilePicture(userId, pictureBytes, processedMimeType, userId);
        LOG.info("Updated profile picture for {}", sessionService.getCurrentUsername());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/" + PICTURE_END_POINT)
    @Produces("text/html")
    public Response deleteProfilePicture() {
        final long userId = Optional.ofNullable(sessionService.getCurrentUser())
                .map(User::getId)
                .orElseThrow(UnauthenticatedException::new);
        userService.removeProfilePicture(userId, userId);   //The current user may only remove their own profile picture

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
    // TODO: move logic to service
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
