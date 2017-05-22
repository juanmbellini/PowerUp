package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.ShelfJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 27/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shelf")
public class ShelfDto extends EntityDto {


    @XmlElement
    private String name;

    @XmlElement
    private String userName;

    @XmlElement
    private Long userId;

    @XmlElement
    private String gamesUrl;

    @XmlElement
    private String userUrl;


    public ShelfDto() {
        // For Jax-RS
    }


    public ShelfDto(Shelf shelf, UriBuilder baseUri) {
        super(shelf.getId());
        this.name = shelf.getName();
        this.userName = shelf.getUser().getUsername();
        this.userId = shelf.getUser().getId();

        // Urls
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(String.valueOf(shelf.getUser().getId()))
                .build().toString();
        this.gamesUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(String.valueOf(shelf.getUser().getId()))
                .path(ShelfJerseyController.END_POINT)
                .path(shelf.getName())
                .path(ShelfJerseyController.GAMES_END_POINT)
                .build().toString();

    }


    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getGamesUrl() {
        return gamesUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    /**
     * Returns a list of {@link ShelfDto} based on the given collection of {@link Shelf}.
     *
     * @param shelves The collection of {@link Shelf}
     * @return A list of {@link ShelfDto}.
     */
    public static List<ShelfDto> createList(Collection<Shelf> shelves, UriBuilder uriBuilder) {
        return shelves.stream().map(shelf -> new ShelfDto(shelf, uriBuilder.clone())).collect(Collectors.toList());
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "shelfGame")
    public static class ShelfGameDto {


        private Long gameId;

        private String gameName;

        private String gameUrl;


        public ShelfGameDto() {
            // For Jax-RS
        }


        public ShelfGameDto(Game game) {
            this.gameId = game.getId();
            this.gameName = game.getName();
            this.gameUrl = ""; // TODO: complete
        }

    }
}
