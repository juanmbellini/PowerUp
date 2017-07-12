package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ShelfDto;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ShelfJerseyController.END_POINT;

/**
 * API endpoint for user shelves management.
 */
@Path(UserJerseyController.END_POINT + "/{userId : \\d+}/" + END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ShelfJerseyController implements UpdateParamsChecker {

    public static final String END_POINT = "shelves";

    public static final String GAMES_END_POINT = "games";

    @Autowired
    private ShelfJerseyController(ShelfService shelfService, SessionService sessionService, UserService userService) {
        this.shelfService = shelfService;
        this.sessionService = sessionService;
        this.userService = userService;
    }


    private final ShelfService shelfService;

    private final UserService userService;

    private final SessionService sessionService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG = LoggerFactory.getLogger(getClass());


    // ================ API methods ================


    // ======== Basic shelf operation ========


    @GET
    public Response getShelves(@QueryParam("orderBy") @DefaultValue("name") final ShelfDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               // Filters
                               @QueryParam("name") @DefaultValue("") final String name,
                               @QueryParam("gameId") @DefaultValue("") final Long gameId,
                               @QueryParam("gameName") @DefaultValue("") final String gameName,
                               // This path param is declared at class level
                               @SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0));
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        shelfService.getUserShelves(userId, name, gameId, gameName, pageNumber, pageSize,
                                sortingType, sortDirection),
                        (shelfPage) -> new GenericEntity<List<ShelfDto>>(ShelfDto.createList(shelfPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("name", name)
                                .addParameter("gameId", gameId)
                                .addParameter("gameName", gameName)
                                .build());
    }

    @GET
    @Path("/{shelfName : .+}")
    public Response getByName(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                              @PathParam("shelfName") final String shelfName) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null));

        final Shelf shelf = shelfService.findByName(userId, shelfName);
        return shelf == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new ShelfDto(shelf, uriInfo.getBaseUriBuilder())).build();
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                final ShelfDto shelfDto) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0));
        if (shelfDto == null) {
            throw new MissingJsonException();
        }
        final Shelf shelf = shelfService.create(userId, shelfDto.getName(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(shelf.getName())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{shelfName : .+}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response updateShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                @PathParam("shelfName") final String shelfName,
                                final ShelfDto shelfDto) {
        if (shelfDto == null) {
            throw new MissingJsonException();
        }
        checkUpdateValues("userId", userId, id -> id > 0, shelfDto.getUserId(),
                (pathId, dtoId) -> dtoId == null || Long.compare(pathId, dtoId) == 0);
        // The shelfName string is just checked if its null because dto and path names will probably be different
        // (The name is the only editable field for a shelf)
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("shelfName", shelfName, name -> name == null));

        shelfService.update(userId, shelfName, shelfDto.getName(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = uriInfo.getBaseUriBuilder()
                .path(UserJerseyController.END_POINT)   // Appends users endpoint
                .path(Long.toString(userId))            // Appends user id
                .path(END_POINT)                        // Appends shelves endpoint
                .path(shelfDto.getName())               // Appends new name
                .build();
        // As endpoint includes shelf name, and shelf name changes (if its not the same) after execution
        // new location is informed by the "Location" header (whether the name changed, or not)
        return Response.noContent().location(uri).build();
    }

    @DELETE
    @Path("/{shelfName : .+}")
    public Response deleteShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                @PathParam("shelfName") final String shelfName) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null));
        shelfService.delete(userId, shelfName,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }


    // ======== Game list operation ========

    @GET
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response listShelfGames(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                   @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName")
                                   final String shelfName,
                                   @QueryParam("orderBy") @DefaultValue("name") final OrderCategory orderCategory,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null));

        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, orderCategory.toString().toLowerCase(), sortDirection,
                        shelfService.getShelfGames(userId, shelfName,
                                pageNumber, pageSize, orderCategory, sortDirection),
                        (gamesPage) -> new GenericEntity<List<ShelfDto.ShelfGameDto>>(ShelfDto.ShelfGameDto
                                .createList(gamesPage.getData(), uriInfo.getBaseUriBuilder())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .build());
    }

    @POST
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response addGameToShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                   @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName")
                                   final String shelfName,
                                   final ShelfDto.ShelfGameDto shelfGameDto) {
        if (shelfGameDto == null) {
            throw new MissingJsonException();
        }

        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null));

        shelfService.addGameToShelf(userId, shelfName, shelfGameDto.getGameId(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        if(userService.getPlayStatuses(userId, shelfGameDto.getGameId(), null, 1, 25, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData().isEmpty()){
            userService.setPlayStatus(userId, shelfGameDto.getGameId(), PlayStatus.NO_PLAY_STATUS, userId);
        }
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(shelfGameDto.getGameId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{shelfName : .+}/" + GAMES_END_POINT + "/{gameId : \\d+}")
    public Response removeGameFromShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId")
                                        final long userId,
                                        @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName")
                                        final String shelfName,
                                        @SuppressWarnings("RSReferenceInspection") @PathParam("gameId")
                                        final long gameId) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null)
                .addParameter("gameId", gameId, id -> id <= 0));

        shelfService.removeGameFromShelf(userId, shelfName, gameId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        if(!belongsToGameList(userId, gameId)){
            deleteFromGameList(userId, gameId);
        }
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response clearShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId")
                               final long userId,
                               @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName")
                               final String shelfName) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, name -> name == null));
        shelfService.clearShelf(userId, shelfName,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    /**
     *
     * @return whether or not the game belongs to the User's GameList.
     */

    private boolean belongsToGameList(final long userId, final long gameId){
        return !userService.getGameScores(userId, gameId, null, 1, 25, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID,SortDirection.ASC).getData().isEmpty()
                || !shelfService.getUserShelves(userId, null, gameId, null, 1, 25, ShelfDao.SortingType.ID, SortDirection.ASC).isEmpty();
    }

    private void deleteFromGameList(long userId, long gameId) {
        userService.removePlayStatus(userId, gameId, userId);
    }
}