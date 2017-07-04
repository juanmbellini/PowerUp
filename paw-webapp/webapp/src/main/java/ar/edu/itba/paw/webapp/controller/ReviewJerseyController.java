package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewService;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ReviewJerseyController.END_POINT;

/**
 * API endpoint for review management.
 */
@Path(END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ReviewJerseyController implements UpdateParamsChecker {

    public static final String END_POINT = "reviews";


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


    // ======== Basic review operation ========


    @GET
    public Response getReviews(@QueryParam("orderBy") @DefaultValue("id") final ReviewDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               // Filters
                               @QueryParam("gameId") @DefaultValue("") final Long gameId,
                               @QueryParam("gameName") @DefaultValue("") final String gameName,
                               @QueryParam("userId") @DefaultValue("") final Long userId,
                               @QueryParam("username") @DefaultValue("") final String username) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber));
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        reviewService.getReviews(gameId, gameName, userId, username, pageNumber, pageSize,
                                sortingType, sortDirection),
                        (reviewPage) -> new GenericEntity<List<ReviewDto>>(ReviewDto.createList(reviewPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("gameId", gameId)
                                .addParameter("gameName", gameName)
                                .addParameter("userId", userId)
                                .addParameter("username", username)
                                .build());
    }

    @GET
    @Path("/{id : \\d+}")
    public Response getById(@PathParam("id") final long id) {
        if (id <= 0) {
            throw new IllegalParameterValueException("id");
        }
        final Review review = reviewService.findById(id);
        return review == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(new ReviewDto(review, uriInfo.getBaseUriBuilder())).build();
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createReview(final ReviewDto reviewDto) {
        if (reviewDto == null) {
            throw new MissingJsonException();
        }
        Review review = reviewService.create(reviewDto.getGameId(), reviewDto.getBody(),
                reviewDto.getStoryScore(), reviewDto.getGraphicsScore(), reviewDto.getAudioScore(),
                reviewDto.getControlsScore(), reviewDto.getFunScore(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(review.getId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id : \\d+}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response updateReview(@PathParam("id") final long reviewId, final ReviewDto reviewDto) {
        checkUpdateValues(reviewId, "id", reviewDto);
        reviewService.update(reviewId, reviewDto.getBody(), reviewDto.getStoryScore(), reviewDto.getGraphicsScore(),
                reviewDto.getAudioScore(), reviewDto.getControlsScore(), reviewDto.getFunScore(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteReview(@PathParam("id") final long reviewId) {
        if (reviewId <= 0) {
            throw new IllegalParameterValueException("id");
        }
        reviewService.delete(reviewId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }


}
