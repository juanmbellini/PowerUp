package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.GameJerseyController;
import ar.edu.itba.paw.webapp.controller.ShelfJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 15/7/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "")
public class ListGameDto {

    @XmlElement
    private GamePartDto game;

    @XmlElement
    private List<ShelfPartDto> shelvesHolding;

    @XmlElement
    private Integer score;

    @XmlElement
    @JsonDeserialize(using = DtoHelper.PlayStatusEnumDeserializer.class)
    private PlayStatus status;


    public ListGameDto() {
        // For Jax-RS
    }

    public ListGameDto(GameWithUserShelvesWrapper wrapper, UriBuilder baseUri) {
        this.game = new GamePartDto(wrapper.getGame(), baseUri.clone());
        this.shelvesHolding = wrapper.getShelves().stream().map(shelf -> new ShelfPartDto(shelf, baseUri.clone()))
                .collect(Collectors.toList());
        this.score = wrapper.getScore();
        this.status = wrapper.getStatus();
    }

    public GamePartDto getGame() {
        return game;
    }

    public List<ShelfPartDto> getShelvesHolding() {
        return shelvesHolding;
    }

    public Integer getScore() {
        return score;
    }

    public PlayStatus getStatus() {
        return status;
    }

    /**
     * Returns a list of {@link ListGameDto} based on the given collection of {@link Game}.
     *
     * @param games The collection of {@link Game}
     * @return A list of {@link ListGameDto}.
     */
    public static List<ListGameDto> createList(Collection<GameWithUserShelvesWrapper> games, UriBuilder uriBuilder) {
        return games.stream().map(game -> new ListGameDto(game, uriBuilder.clone())).collect(Collectors.toList());
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "creator")
    public static class GamePartDto extends EntityDto {


        @XmlElement
        private String name;

        @XmlElement
        private String coverPictureUrl;

        @XmlElement
        private Double avgScore;

        @XmlElement
        private String gameUrl;


        public GamePartDto() {
            // For Jax-RS
        }


        public GamePartDto(Game game, UriBuilder baseUri) {
            super(game.getId());
            this.name = game.getName();
            this.coverPictureUrl = game.getCoverPictureUrl();
            this.avgScore = game.getAvgScore();
            this.gameUrl = baseUri.clone()
                    .path(GameJerseyController.END_POINT)
                    .path(Long.toString(game.getId()))
                    .build().toString();
        }

        public String getName() {
            return name;
        }

        public String getCoverPictureUrl() {
            return coverPictureUrl;
        }

        public Double getAvgScore() {
            return avgScore;
        }

        public String getGameUrl() {
            return gameUrl;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "creator")
    public static class ShelfPartDto extends EntityDto {


        @XmlElement
        private String name;

        @XmlElement
        private String shelfUrl;


        public ShelfPartDto() {
            // For Jax-RS
        }


        public ShelfPartDto(Shelf shelf, UriBuilder baseUri) {
            super(shelf.getId());
            this.name = shelf.getName();
            this.shelfUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(shelf.getUser().getId()))
                    .path(ShelfJerseyController.END_POINT)
                    .path(shelf.getName())
                    .build().toString();
        }

        public String getName() {
            return name;
        }

        public String getShelfUrl() {
            return shelfUrl;
        }
    }
}
