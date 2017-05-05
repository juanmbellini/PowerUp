package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;
import org.hibernate.Hibernate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.LinkedList;
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


    @XmlElement
    private List<ShelfGameDto> games;


    public ShelfDto() {
        // For Jax-RS
    }


    public ShelfDto(Shelf shelf) {
        super(shelf.getId());
        this.name = shelf.getName();
        this.userName = shelf.getUser().getUsername();
        this.userId = shelf.getUser().getId();
        this.games = !Hibernate.isInitialized(shelf.getShelfGames()) ? new LinkedList<>() :
                shelf.getGames().stream().map(ShelfGameDto::new).collect(Collectors.toList());

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

    public List<ShelfGameDto> getGames() {
        return games;
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
    public static List<ShelfDto> createList(Collection<Shelf> shelves) {
        return shelves.stream().map(ShelfDto::new).collect(Collectors.toList());
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
