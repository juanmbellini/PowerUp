package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.ShelfDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.Shelf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

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
                               @SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                               @QueryParam("userName") @DefaultValue("") final String userName) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0));
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        shelfService.getShelves(name, gameId, gameName, userId, userName, pageNumber, pageSize,
                                sortingType, sortDirection),
                        (shelfPage) -> new GenericEntity<List<ShelfDto>>(ShelfDto.createList(shelfPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("name", name)
                                .addParameter("gameId", gameId)
                                .addParameter("gameName", gameName)
                                .addParameter("userId", userId)
                                .addParameter("userName", userName)
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
                        (gamesPage) -> new GenericEntity<List<GameDto>>(GameDto.createList(gamesPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .build());
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createShelf(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                final ShelfDto shelfDto) {
        if (shelfDto == null) {
            throw new MissingJsonException();
        }
        final Shelf shelf = shelfService.create(userId, shelfDto.getName(), sessionService.getCurrentUser());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(shelf.getId())).build();
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

        shelfService.update(userId, shelfName, shelfDto.getName(), sessionService.getCurrentUser());
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


}