package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.ThreadDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadService;
import ar.edu.itba.paw.webapp.model.Thread;
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

/**
 * API endpoint for thread management.
 */
@Path("threads")
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ThreadJerseyController implements UpdateParamsChecker {


    @Autowired
    private ThreadJerseyController(ThreadService threadService, SessionService sessionService) {
        this.threadService = threadService;
        this.sessionService = sessionService;
    }

    private final ThreadService threadService;

    private final SessionService sessionService;

    @Context
    private UriInfo uriInfo;

    private Logger LOG = LoggerFactory.getLogger(getClass());


    // ================ API methods ================


    // ======== Basic thread operation ========

    @GET
    public Response getThreads(@QueryParam("orderBy") @DefaultValue("newest") final ThreadDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               // Filters
                               @QueryParam("title") @DefaultValue("") final String title,
                               @QueryParam("userId") @DefaultValue("") final Long userId,
                               @QueryParam("username") @DefaultValue("") final String username) {
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        threadService.getThreads(title, userId, username,
                                pageNumber, pageSize, sortingType, sortDirection),
                        (threadPage) -> new GenericEntity<List<ThreadDto>>(ThreadDto.createList(threadPage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("title", title)
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
        return Optional.ofNullable(threadService.findById(id))
                .map(thread -> Response.ok(new ThreadDto(thread)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createThread(final ThreadDto threadDto) {
        if (threadDto == null) {
            throw new MissingJsonException();
        }
        final Thread thread = threadService.create(threadDto.getTitle(), getCurrentUserId(),
                threadDto.getBody());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(thread.getId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }


    @PUT
    @Path("/{id : \\d+}")
    public Response updateThread(@PathParam("id") final long threadId,
                                 final ThreadDto threadDto) {
        checkUpdateValues(threadId, "id", threadDto);
        threadService.update(threadId, threadDto.getTitle(), threadDto.getBody(), getCurrentUserId()); // TODO: updater
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteThread(@PathParam("id") final long threadId) {
        threadService.delete(threadId, getCurrentUserId()); // TODO: updater
        return Response.noContent().build();
    }


    // ================ Helper methods ================


    private long getCurrentUserId() {
        User user = this.sessionService.getCurrentUser();
        return user == null ? -1 : user.getId();
    }


}
