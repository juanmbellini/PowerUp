package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Juan Marcos Bellini on 8/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Path("games")
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
    public Response getAllGames(@QueryParam("orderCategory") final OrderCategory orderCategory,
                                @QueryParam("ascending") final boolean ascending,
                                @QueryParam("pageSize") final int pageSize,
                                @QueryParam("pageNumber") final int pageNumber) {
        Page<Game> games = gameService.searchGames("", new HashMap<>(), orderCategory, ascending, pageSize, pageNumber);
        return Response
                .ok(new GenericEntity<List<GameDto>>(GameDto.createList(games.getData())) {
                })
                .header("X-TotalPages", games.getTotalPages())
                .header("X-AmountOfElements", games.getAmountOfElements())
                .header("X-OverallAmountOfElements", games.getOverAllAmountOfElements())
                .header("X-PageNumber", games.getPageNumber())
                .header("X-PageSize", games.getPageSize())
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
}
