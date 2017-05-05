package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.FilterCategoryDto;
import ar.edu.itba.paw.webapp.dto.FilterValueDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan Marcos Bellini on 8/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Path("/games")
@Produces(value = {MediaType.APPLICATION_JSON})
public class GameJerseyController {

    private GameService gameService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public GameJerseyController(GameService gameService) {
        this.gameService = gameService;
    }


    @GET
    @Path("/")
    public Response getGames(@QueryParam("orderBy") @DefaultValue("name") final OrderCategory orderCategory,
                             @QueryParam("sortDirection") @DefaultValue("ASC") final SortDirection sortDirection,
                             @QueryParam("pageSize") @DefaultValue("25") final int pageSize,
                             @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                             // Filters
                             @QueryParam("name") @DefaultValue("") final String name,
                             @QueryParam("publisher") final List<String> publishers,
                             @QueryParam("developer") final List<String> developers,
                             @QueryParam("genre") final List<String> genres,
                             @QueryParam("keyword") final List<String> keywords,
                             @QueryParam("platform") final List<String> platforms) {

        // TODO: Check params once chore/error-system is merged
        return JerseyControllerHelper
                .createCollectionGetResponse(uriInfo, orderCategory.toString().toLowerCase(), sortDirection,
                        gameService.searchGames(name,
                                createFiltersMap(publishers, developers, genres, keywords, platforms),
                                orderCategory, sortDirection == SortDirection.ASC, pageSize, pageNumber),
                        (gamePage) -> new GenericEntity<List<GameDto>>(GameDto.createList(gamePage.getData())) {
                        },
                        JerseyControllerHelper.getParameterMapBuilder().clear()
                                .addParameter("name", name)
                                .addParameter("publisher", publishers.toArray())
                                .addParameter("developer", developers.toArray())
                                .addParameter("genre", genres.toArray())
                                .addParameter("keyword", keywords.toArray())
                                .addParameter("platform", platforms.toArray())
                                .build());

    }

    @GET
    @Path("/{id : \\d+}")
    public Response findById(@PathParam("id") final long id) {
        if (id <= 0) {
//            throw new IllegalParameterValueException(PathParam.class, "id", "");
            // TODO: uncomment above and remove below when merging chore/error-system
            throw new IllegalArgumentException();
        }
        final Game game = gameService.findById(id);
        return game == null ?
                Response.status(Response.Status.NOT_FOUND).build() : Response.ok(new GameDto(game)).build();
    }


    @GET
    @Path("/filters")
    public Response getFilters() {
        return Response
                .ok(new GenericEntity<List<FilterCategoryDto>>(FilterCategoryDto.createList(uriInfo)) {
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


    /**
     * Create a filers map.
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

    /**
     * Creates an {@link URI} based on the given {@link UriInfo}, including 'orderCategory', 'ascending', 'pageSize',
     * 'pageNumber', 'name', 'publisher', 'developer', 'genre', 'keyword' and 'platform' as query params.
     * For 'publisher', 'developer', 'genre', 'keyword' and 'platform', they can appear more than once.
     * Values for these params are based on the given arguments of this method.
     *
     * @param uriInfo       The {@link UriInfo} from which the absolute path is taken.
     * @param orderCategory The order category for the query param.
     * @param ascending     The ascending condition for the 'ascending' query param.
     * @param pageSize      The page size for the query param.
     * @param pageNumber    The page number for the query param.
     * @param name          The name for the query param.
     * @param publishers    The List of publishers for the query params
     *                      (if it has more than one element, the query param is repeated).
     * @param developers    The List of developers for the query params
     *                      (if it has more than one element, the query param is repeated).
     * @param genres        The List of genres for the query params
     *                      (if it has more than one element, the query param is repeated).
     * @param keywords      The List of keywords for the query params
     *                      (if it has more than one element, the query param is repeated).
     * @param platforms     The List of platforms for the query params
     *                      (if it has more than one element, the query param is repeated).
     * @return The created {@link URI}.
     */
    private static URI createGetAllGamesUri(final UriInfo uriInfo, final OrderCategory orderCategory,
                                            final boolean ascending, final int pageSize, final int pageNumber,
                                            final String name, final List<String> publishers,
                                            final List<String> developers, final List<String> genres,
                                            final List<String> keywords, final List<String> platforms) {
        return uriInfo.getAbsolutePathBuilder()
                .queryParam("orderCategory", orderCategory)
                .queryParam("ascending", ascending)
                .queryParam("pageSize", pageSize)
                .queryParam("pageNumber", pageNumber)
                .queryParam("name", name)
                .queryParam("publisher", publishers.toArray())
                .queryParam("developer", developers.toArray())
                .queryParam("genre", genres.toArray())
                .queryParam("keyword", keywords.toArray())
                .queryParam("platform", platforms.toArray())
                .build();
    }

}
