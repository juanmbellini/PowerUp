package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewService;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
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
@Path("reviews")
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ReviewJerseyController {


    @Autowired
    private ReviewJerseyController(ReviewService reviewService, SessionService sessionService) {
        this.reviewService = reviewService;
        this.sessionService = sessionService;
    }

    private final ReviewService reviewService;

    private final SessionService sessionService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG = LoggerFactory.getLogger(getClass());


    // ================ API methods ================


    // ======== Basic shelf operation ========


    @GET
    public Response getReviews(@QueryParam("orderBy") @DefaultValue("id") final ReviewDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               // Filters
                               @QueryParam("gameId") @DefaultValue("") final Long gameId,
                               @QueryParam("gameName") @DefaultValue("") final String gameName,
                               @QueryParam("gameId") @DefaultValue("") final Long userId,
                               @QueryParam("gameName") @DefaultValue("") final String userName) {

        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        reviewService.getReviews(gameId, gameName, userId, userName, pageNumber, pageSize,
                                sortingType, sortDirection),
                        (reviewPage) -> new GenericEntity<List<ReviewDto>>(ReviewDto.createList(reviewPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("gameId", gameId)
                                .addParameter("gameName", gameName)
                                .addParameter("gameId", userId)
                                .addParameter("gameName", userName)
                                .build());
    }


}
