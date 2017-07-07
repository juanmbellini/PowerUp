package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.dto.ThreadDto;
import ar.edu.itba.paw.webapp.exceptions.IllegalParameterValueException;
import ar.edu.itba.paw.webapp.exceptions.MissingJsonException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ThreadJerseyController.END_POINT;

/**
 * API endpoint for thread management.
 */
@Path(END_POINT)
@Component
@Produces(value = {MediaType.APPLICATION_JSON,})
public class ThreadJerseyController implements UpdateParamsChecker {

    public static final String END_POINT = "threads";

    public static final String COMMENTS_END_POINT = "comments";

    public static final String REPLIES_END_POINT = "replies";

    public static final String LIKES_END_POINT = "likes";

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
    public Response getThreads(@QueryParam("orderBy") @DefaultValue("id") final ThreadDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               // Filters
                               @QueryParam("title") @DefaultValue("") final String title,
                               @QueryParam("userId") @DefaultValue("") final Long userId,
                               @QueryParam("username") @DefaultValue("") final String username) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber));
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        threadService.getThreads(title, userId, username,
                                pageNumber, pageSize, sortingType, sortDirection),
                        (threadPage) -> new GenericEntity<List<ThreadDto>>(ThreadDto.createList(threadPage.getData(),
                                uriInfo.getBaseUriBuilder())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("title", title)
                                .addParameter("userId", userId)
                                .addParameter("username", username)
                                .build());
    }

    @GET
    @Path("/{threadId : \\d+}")
    public Response getById(@PathParam("threadId") final long id) {
        if (id <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        return Optional.ofNullable(threadService.findById(id))
                .map(thread -> Response.ok(new ThreadDto(thread, uriInfo.getBaseUriBuilder())).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createThread(final ThreadDto threadDto) {
        if (threadDto == null) {
            throw new MissingJsonException();
        }
        final Thread thread = threadService.create(threadDto.getTitle(), threadDto.getBody(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(thread.getId())).build();
        return Response.created(uri).status(Response.Status.CREATED).build();
    }


    @PUT
    @Path("/{threadId : \\d+}")
    public Response updateThread(@PathParam("threadId") final long threadId,
                                 final ThreadDto threadDto) {
        checkUpdateValues(threadId, "threadId", threadDto);
        threadService.update(threadId, threadDto.getTitle(), threadDto.getBody(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{threadId : \\d+}")
    public Response deleteThread(@PathParam("threadId") final long threadId) {
        if (threadId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.delete(threadId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @OPTIONS
    @Path("/")
    public Response threadsOptions() {
        return Response.noContent()
                .type(MediaType.TEXT_HTML)  // Required by CORS
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Allow-Headers", "Content-Type")    // Required by CORS
                .build();
    }

    @OPTIONS
    @Path("/{threadId : \\d+}/")
    public Response threadOptions(@PathParam("threadId") final long threadId) {
        return Response.noContent()
                .type(MediaType.TEXT_HTML) // Required by CORS
                .header("Access-Control-Allow-Methods", "PUT,DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type") // Required by CORS
                .build();
    }


    // ==== Thread likes ====

    @PUT
    @Path("/{threadId : \\d+}/" + LIKES_END_POINT)
    public Response likeThread(@SuppressWarnings("RSReferenceInspection") @PathParam("threadId") final long threadId) {
        if (threadId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.likeThread(threadId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{threadId : \\d+}/" + LIKES_END_POINT)
    public Response unlikeThread(@SuppressWarnings("RSReferenceInspection") @PathParam("threadId") final long threadId) {
        if (threadId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.unlikeThread(threadId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @OPTIONS
    @Path("/{threadId : \\d+}/" + LIKES_END_POINT)
    public Response threadLikeOptions(@SuppressWarnings("RSReferenceInspection") @PathParam("threadId") final long threadId) {
        return Response.noContent()
                .type(MediaType.TEXT_HTML)  // Required by CORS
                .header("Access-Control-Allow-Methods", "PUT,DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type")    // Required by CORS
                .build();
    }


    // ======== Thread comments ========


    @GET
    @Path("/{threadId : \\d+}/" + COMMENTS_END_POINT)
    public Response getThreadComments(@QueryParam("orderBy") @DefaultValue("date")
                                      final CommentDao.SortingType sortingType,
                                      @QueryParam("sortDirection") @DefaultValue("ASC")
                                      final SortDirection sortDirection,
                                      @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                                      @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                      @SuppressWarnings("RSReferenceInspection") @PathParam("threadId")
                                      final long threadId) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("threadId", threadId, id -> id <= 0));
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        threadService.getThreadComments(threadId, pageNumber, pageSize, sortingType, sortDirection),
                        (commentPage) -> new GenericEntity<List<CommentDto>>(CommentDto
                                .createList(commentPage.getData(), uriInfo.getBaseUriBuilder())) {
                        }, JerseyControllerHelper.getParameterMapBuilder().clear().build());
    }

    @GET
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}")
    public Response getCommentById(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                                   final long commentId) {
        if (commentId <= 0) {
            throw new IllegalParameterValueException("commentId");
        }
        return Optional.ofNullable(threadService.findCommentById(commentId))
                .map(thread -> Response.ok(new CommentDto(thread, uriInfo.getBaseUriBuilder())).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{threadId : \\d+}/" + COMMENTS_END_POINT)
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createComment(@SuppressWarnings("RSReferenceInspection") @PathParam("threadId")
                                  final long threadId,
                                  final CommentDto commentDto) {
        if (commentDto == null) {
            throw new MissingJsonException();
        }
        final Comment comment = threadService.comment(threadId, commentDto.getBody(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = getNewCommentLocation(uriInfo.getBaseUriBuilder(), comment);
        return Response.created(uri).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}")
    public Response editComment(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId") final long commentId,
                                final CommentDto commentDto) {
        checkUpdateValues(commentId, "commentId", commentDto);
        threadService.editComment(commentId, commentDto.getBody(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}")
    public Response deleteComment(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                                  final long commentId) {
        if (commentId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.deleteComment(commentId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @GET
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}" + "/" + REPLIES_END_POINT)
    public Response getReplies(@QueryParam("orderBy") @DefaultValue("date") final CommentDao.SortingType sortingType,
                               @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                               @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                               @SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                               final long commentId) {
        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber)
                .addParameter("commentId", commentId, id -> id <= 0));
        return JerseyControllerHelper
                .createCollectionGetResponse(
                        uriInfo, sortingType.toString().toLowerCase(), sortDirection,
                        threadService.getCommentReplies(commentId, pageNumber, pageSize, sortingType, sortDirection),
                        (commentPage) -> new GenericEntity<List<CommentDto>>(CommentDto
                                .createList(commentPage.getData(), uriInfo.getBaseUriBuilder())) {
                        }, JerseyControllerHelper.getParameterMapBuilder().clear().build());
    }


    @POST
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}" + "/" + REPLIES_END_POINT)
    public Response replyComment(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                                 final long commentId,
                                 final CommentDto commentDto) {
        if (commentDto == null) {
            throw new MissingJsonException();
        }
        final Comment reply = threadService.replyToComment(commentId, commentDto.getBody(),
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        final URI uri = getNewCommentLocation(uriInfo.getBaseUriBuilder(), reply);
        return Response.created(uri).status(Response.Status.CREATED).build();
    }


    // ==== Comment likes ====

    @PUT
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}" + "/" + LIKES_END_POINT)
    public Response likeComment(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                                final long commentId) {
        if (commentId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.likeComment(commentId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/" + COMMENTS_END_POINT + "/{commentId : \\d+}" + "/" + LIKES_END_POINT)
    public Response unlikeComment(@SuppressWarnings("RSReferenceInspection") @PathParam("commentId")
                                  final long commentId) {
        if (commentId <= 0) {
            throw new IllegalParameterValueException("threadId");
        }
        threadService.unlikeComment(commentId,
                Optional.ofNullable(sessionService.getCurrentUser()).orElseThrow(UnauthenticatedException::new));
        return Response.noContent().build();
    }


    // ================ Helpers ================


    /**
     * Creates the {@link URI} of the location of a new {@link Comment}, using the given {@link UriBuilder} as a base.
     *
     * @param uriBuilder The {@link UriBuilder} containing the base.
     * @param comment    The new comment.
     * @return The created {@link URI}.
     */
    private static URI getNewCommentLocation(UriBuilder uriBuilder, Comment comment) {
        return uriBuilder
                .path(END_POINT)
                .path(COMMENTS_END_POINT)
                .path(String.valueOf(comment.getId()))
                .build();
    }
}
