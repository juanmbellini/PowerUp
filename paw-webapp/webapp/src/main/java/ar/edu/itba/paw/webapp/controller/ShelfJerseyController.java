package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.ShelfDto;
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
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ShelfJerseyController.END_POINT;

/**
 * API endpoint for user management.
 */
@Path(UserJerseyController.END_POINT + "/{userId : \\d+}/" + END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ShelfJerseyController {

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
    @Path("/{shelf-name}")
    public Response getByName(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                              @PathParam("shelf-name") final String shelfName) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper.getParametersWrapper()
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelf-name", shelfName, name -> name == null));

        final Shelf shelf = shelfService.findByName(userId, shelfName);
        return shelf == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new ShelfDto(shelf, uriInfo.getBaseUriBuilder())).build();
    }


    @GET
    @Path("/{shelf-name}/" + GAMES_END_POINT)
    public Response listShelfGames(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                                   @SuppressWarnings("RSReferenceInspection") @PathParam("shelf-name")
                                   final String shelfName,
                                   @QueryParam("orderBy") @DefaultValue("name") final OrderCategory orderCategory,
                                   @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                                   @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("userId", userId, id -> id <= 0)
                .addParameter("shelf-name", shelfName, name -> name == null));

        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, orderCategory.toString().toLowerCase(), sortDirection,
                        shelfService.getShelfGames(userId, shelfName,
                                pageNumber, pageSize, orderCategory, sortDirection),
                        (gamesPage) -> new GenericEntity<List<GameDto>>(GameDto.createList(gamesPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .build());
    }


}
