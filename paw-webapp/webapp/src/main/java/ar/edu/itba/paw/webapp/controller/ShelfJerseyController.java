package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ShelfDto;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private ShelfJerseyController(ShelfService shelfService, SessionService sessionService) {
        this.shelfService = shelfService;
        this.sessionService = sessionService;
    }


    private final ShelfService shelfService;

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

        final Page<Shelf> shelves = shelfService.getUserShelves(userId, name, gameId, gameName,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<String, Object> paramsForPaginationMap = JerseyControllerHelper.getParameterMapBuilder().clear()
                .addParameter("name", name)
                .addParameter("gameId", gameId)
                .addParameter("gameName", gameName)
                .build();
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection, shelves,
                        (shelfPage) -> new GenericEntity<List<ShelfDto>>(ShelfDto.createList(shelfPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        }, paramsForPaginationMap);
    }

    @GET
    @Path("/{shelfName : .+}")
    public Response getByName(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                              @PathParam("shelfName") final String shelfName) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, Objects::isNull));

        return Optional.ofNullable(shelfService.findByName(userId, shelfName))
                .map(shelf -> Response.ok(new ShelfDto(shelf, uriInfo.getBaseUriBuilder())).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
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
                .addParameter("shelfName", shelfName, Objects::isNull));

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
                .addParameter("shelfName", shelfName, Objects::isNull));

        shelfService.delete(userId, shelfName,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }


    // ======== Game list operation ========

    @GET
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response listShelfGames(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                   @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName") final String shelfName,
                                   @QueryParam("orderBy") @DefaultValue("name") final OrderCategory orderCategory,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, Objects::isNull));

        final Page<Game> games = shelfService.getShelfGames(userId, shelfName,
                pageNumber, pageSize, orderCategory, sortDirection);
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, orderCategory.toString().toLowerCase(), sortDirection, games,
                        (gamesPage) -> new GenericEntity<List<ShelfDto.ShelfGameDto>>(ShelfDto.ShelfGameDto
                                .createList(gamesPage.getData(), uriInfo.getBaseUriBuilder())) {
                        });
    }

    @POST
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response addGameToShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                   @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName") final String shelfName,
                                   final ShelfDto.ShelfGameDto shelfGameDto) {
        if (shelfGameDto == null) {
            throw new MissingJsonException();
        }
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, Objects::isNull));

        shelfService.addGameToShelf(userId, shelfName, shelfGameDto.getGameId(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(shelfGameDto.getGameId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{shelfName : .+}/" + GAMES_END_POINT + "/{gameId : \\d+}")
    public Response removeGameFromShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                        @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName") final String shelfName,
                                        @SuppressWarnings("RSReferenceInspection") @PathParam("gameId") final long gameId) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, Objects::isNull)
                .addParameter("gameId", gameId, id -> id <= 0));

        shelfService.removeGameFromShelf(userId, shelfName, gameId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response clearShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                               @SuppressWarnings("RSReferenceInspection") @PathParam("shelfName") final String shelfName) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelfName", shelfName, Objects::isNull));

        shelfService.clearShelf(userId, shelfName,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @OPTIONS
    public Response shelvesOptions() {
        Response.ResponseBuilder result = Response
                .ok()
                .type(MediaType.TEXT_HTML)                                              // Required by CORS
                .header("Access-Control-Allow-Methods", "PUT,DELETE,OPTIONS,POST,GET")
                .header("Access-Control-Allow-Headers", "Content-Type");  // Required by CORS
        return result.build();
    }

    @OPTIONS
    @Path("/{shelfName : .+}")
    public Response shelvesOptions2() {
        return shelvesOptions();
    }

    @OPTIONS
    @Path("/{shelfName : .+}/" + GAMES_END_POINT)
    public Response shelvesOptions3() {
        return shelvesOptions();
    }

    @OPTIONS
    @Path("/{shelfName : .+}/" + GAMES_END_POINT + "/{gameId : \\d+}")
    public Response shelvesOptions4() {
        return shelvesOptions();
    }
}