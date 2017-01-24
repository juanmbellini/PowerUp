package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.FilterCategoryDto;
import ar.edu.itba.paw.webapp.dto.FilterValueDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Response getAllGames(@QueryParam("orderCategory") @DefaultValue("name") final OrderCategory orderCategory,
                                @QueryParam("ascending") @DefaultValue("true") final boolean ascending,
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

        Page<Game> games = gameService.searchGames(name,
                createFiltersMap(publishers, developers, genres, keywords, platforms),
                orderCategory, ascending, pageSize, pageNumber);

        URI prevPage = pageNumber == 1 ? null :
                uriInfo.getAbsolutePathBuilder()
                        .queryParam("orderCategory", orderCategory)
                        .queryParam("ascending", ascending)
                        .queryParam("pageSize", pageSize)
                        .queryParam("pageNumber", pageNumber - 1)
                        .queryParam("name", name)
                        .queryParam("publisher", publishers.toArray())
                        .queryParam("developer", developers.toArray())
                        .queryParam("genre", genres.toArray())
                        .queryParam("keyword", keywords.toArray())
                        .queryParam("platform", platforms.toArray())
                        .build();
        URI nextPage = pageNumber == games.getTotalPages() ? null :
                uriInfo.getAbsolutePathBuilder()
                        .queryParam("orderCategory", orderCategory)
                        .queryParam("ascending", ascending)
                        .queryParam("pageSize", pageSize)
                        .queryParam("pageNumber", pageNumber + 1)
                        .queryParam("name", name)
                        .queryParam("publisher", publishers.toArray())
                        .queryParam("developer", developers.toArray())
                        .queryParam("genre", genres.toArray())
                        .queryParam("keyword", keywords.toArray())
                        .queryParam("platform", platforms.toArray())
                        .build();
        URI firstPage = uriInfo.getAbsolutePathBuilder()
                .queryParam("orderCategory", orderCategory)
                .queryParam("ascending", ascending)
                .queryParam("pageSize", pageSize)
                .queryParam("pageNumber", 1)
                .queryParam("name", name)
                .queryParam("publisher", publishers.toArray())
                .queryParam("developer", developers.toArray())
                .queryParam("genre", genres.toArray())
                .queryParam("keyword", keywords.toArray())
                .queryParam("platform", platforms.toArray())
                .build();
        URI lastPage = uriInfo.getAbsolutePathBuilder()
                .queryParam("orderCategory", orderCategory)
                .queryParam("ascending", ascending)
                .queryParam("pageSize", pageSize)
                .queryParam("pageNumber", games.getTotalPages())
                .queryParam("name", name)
                .queryParam("publisher", publishers.toArray())
                .queryParam("developer", developers.toArray())
                .queryParam("genre", genres.toArray())
                .queryParam("keyword", keywords.toArray())
                .queryParam("platform", platforms.toArray())
                .build();
        return Response
                .ok(new GenericEntity<List<GameDto>>(GameDto.createList(games.getData())) {
                })
                .header("X-Total-Pages", games.getTotalPages())
                .header("X-Amount-Of-Elements", games.getAmountOfElements())
                .header("X-Overall-Amount-Of-Elements", games.getOverAllAmountOfElements())
                .header("X-Page-Number", games.getPageNumber())
                .header("X-Page-Size", games.getPageSize())
                .header("X-Prev-Page-Url", prevPage == null ? "" : prevPage.toString())
                .header("X-Next-Page-Url", nextPage == null ? "" : nextPage.toString())
                .header("X-First-Page-Url", firstPage.toString())
                .header("X-Last-Page-Url", lastPage.toString())
                .build();

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
                        .createList(gameService
                                .getFiltersByType(filterCategory).stream().collect(Collectors.toList()))) {
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

}
