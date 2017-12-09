package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.FilterValueDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.TwitchStreamDto;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.TwitchService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.TwitchStream;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.paw.webapp.controller.GameJerseyController.END_POINT;

/**
 * Created by Juan Marcos Bellini on 8/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Path(END_POINT)
@Produces(value = {MediaType.APPLICATION_JSON})
@Component
public class GameJerseyController {

    public static final String END_POINT = "games";

    /**
     * A {@link GameService} to operate with {@link Game}s.
     */
    private final GameService gameService;

    /**
     * A {@link TwitchService} that provides Twitch services.
     */
    private final TwitchService twitchService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public GameJerseyController(GameService gameService, TwitchService twitchService) {
        this.gameService = gameService;
        this.twitchService = twitchService;
    }


    // ================ API methods ================


    // ======== Basic game operation ========


    @GET
    public Response getGames(@QueryParam("orderBy") @DefaultValue("name") final OrderCategory orderCategory,
                             @QueryParam("sortDirection") @DefaultValue("asc") final SortDirection sortDirection,
                             @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                             @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                             // Filters
                             @QueryParam("name") @DefaultValue("") final String name,
                             @QueryParam("publisher") final List<String> publishers,
                             @QueryParam("developer") final List<String> developers,
                             @QueryParam("genre") final List<String> genres,
                             @QueryParam("keyword") final List<String> keywords,
                             @QueryParam("platform") final List<String> platforms) {

        JerseyControllerHelper.checkParameters(JerseyControllerHelper
                .getPaginationReadyParametersWrapper(pageSize, pageNumber));

        final Page<Game> games = gameService.searchGames(name,
                createFiltersMap(publishers, developers, genres, keywords, platforms),
                orderCategory, sortDirection == SortDirection.ASC, pageSize, pageNumber);
        final Map<String, Object> paramsForPaginationMap = JerseyControllerHelper.getParameterMapBuilder().clear()
                .addParameter("name", name)
                .addParameter("publisher", publishers.toArray())
                .addParameter("developer", developers.toArray())
                .addParameter("genre", genres.toArray())
                .addParameter("keyword", keywords.toArray())
                .addParameter("platform", platforms.toArray())
                .build();
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, orderCategory.toString().toLowerCase(), sortDirection, games,
                        (gamePage) -> new GenericEntity<List<GameDto>>(GameDto.createList(gamePage.getData())) {
                        }, paramsForPaginationMap);

    }

    @GET
    @Path("/{id : \\d+}")
    public Response findById(@PathParam("id") final long id) {
        JerseyControllerHelper.checkParameter("id", id, value -> value <= 0);

        return Optional.ofNullable(gameService.findById(id))
                .map(game -> Response.ok(new GameDto(game)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/random")
    public Response getRandomGame() {
        final Game game = gameService.getRandomGame(); // Will never be null.
        final URI gameUri = uriInfo.getBaseUriBuilder().path(END_POINT).path(Long.toString(game.getId())).build();

        return Response.temporaryRedirect(gameUri)
                .entity(new GameDto(game))
                .build();
    }

    @GET
    @Path("/{id : \\d+}/related-games")
    public Response getRelatedGames(@PathParam("id") final long gameId) {
        JerseyControllerHelper.checkParameter("id", gameId, value -> value <= 0);

        final Set<FilterCategory> filters = Stream.of(FilterCategory.platform, FilterCategory.genre)
                .collect(Collectors.toSet());
        final Collection<Game> relatedGames = gameService.findRelatedGames(gameId, filters);
        return Response.ok(new GenericEntity<List<GameDto>>(GameDto.createList(relatedGames)) {
        }).build();
    }

    @GET
    @Path("/filters/{type}")
    public Response getFiltersByType(@PathParam("type") final FilterCategory filterCategory) {
        return Response
                .ok(new GenericEntity<List<FilterValueDto>>(FilterValueDto
                        .createList(gameService.getFiltersByType(filterCategory))) {
                })
                .build();
    }


    // ======== Twitch ========

    @GET
    @Path("/{id : \\d+}/twitch")
    public Response getGameTwitchStreams(@PathParam("id") final long id) {
        JerseyControllerHelper.checkParameter("id", id, value -> value <= 0);

        final List<TwitchStream> streams = twitchService.getStreamsByGameId(id);
        final List<TwitchStreamDto> streamDtos = streams.stream()
                .map(TwitchStreamDto::new)
                .collect(Collectors.toList());

        return Response.ok(streamDtos).build();
    }


    // ================ Helper methods ================

    /**
     * Create a filters map.
     *
     * @param publishers The list with the publishers values.
     * @param developers The list with the developers values.
     * @param genres     The list with the genres values.
     * @param keywords   The list with the keywords values.
     * @param platforms  The list with the platforms values.
     * @return The filters map.
     */
    private static Map<FilterCategory, List<String>> createFiltersMap(List<String> publishers, List<String> developers,
                                                                      List<String> genres, List<String> keywords,
                                                                      List<String> platforms) {
        final Map<FilterCategory, List<String>> filters = new HashMap<>();
        filters.put(FilterCategory.publisher, publishers);
        filters.put(FilterCategory.developer, developers);
        filters.put(FilterCategory.genre, genres);
        filters.put(FilterCategory.keyword, keywords);
        filters.put(FilterCategory.platform, platforms);
        return filters;
    }

}
