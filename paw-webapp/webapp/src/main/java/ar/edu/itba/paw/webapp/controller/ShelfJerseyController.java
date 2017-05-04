package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ShelfDto;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * API endpoint for user management.
 */
@Path("shelves")
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ShelfJerseyController {


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
                               @QueryParam("gameId") @DefaultValue("") final Long userId,
                               @QueryParam("gameName") @DefaultValue("") final String userName) {

        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        shelfService.getShelves(name, gameId, gameName, userId, userName, pageNumber, pageSize,
                                sortingType, sortDirection),
                        (shelfPage) -> new GenericEntity<List<ShelfDto>>(ShelfDto.createList(shelfPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("name", name)
                                .addParameter("gameId", gameId)
                                .addParameter("gameName", gameName)
                                .addParameter("gameId", userId)
                                .addParameter("gameName", userName)
                                .build());
    }


}
