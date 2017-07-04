package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.GameJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Review;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 4/5/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "review")
public class ReviewDto extends EntityDto {


    @XmlElement
    private Long userId;

    @XmlElement
    private String username;

    @XmlElement
    private Long gameId;

    @XmlElement
    private String gameName;

    @XmlElement
    private String gameCoverPictureUrl;

    @XmlElement
    private String date;

    @XmlElement
    private String body;

    @XmlElement
    private Integer storyScore;

    @XmlElement
    private Integer graphicsScore;

    @XmlElement
    private Integer audioScore;

    @XmlElement
    private Integer controlsScore;

    @XmlElement
    private Integer funScore;

    @XmlElement
    private String userUrl;

    @XmlElement
    private String gameUrl;


    public ReviewDto() {
        // For Jax-RS
    }

    public ReviewDto(Review review, UriBuilder baseUri) {
        super(review.getId());
        this.userId = review.getUser().getId();
        this.username = review.getUser().getUsername();
        this.gameId = review.getGame().getId();
        this.gameName = review.getGame().getName();
        this.gameCoverPictureUrl = review.getGame().getCoverPictureUrl();
        this.date = review.getDate().toString();
        this.body = review.getReview();
        this.storyScore = review.getStoryScore();
        this.graphicsScore = review.getGraphicsScore();
        this.audioScore = review.getAudioScore();
        this.controlsScore = review.getControlsScore();
        this.funScore = review.getFunScore();

        // Urls
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(String.valueOf(review.getUser().getId()))
                .build().toString();
        this.gameUrl = baseUri.clone()
                .path(GameJerseyController.END_POINT)
                .path(String.valueOf(review.getGame().getId()))
                .build().toString();
    }


    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameCoverPictureUrl() {
        return gameCoverPictureUrl;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public Integer getStoryScore() {
        return storyScore;
    }

    public Integer getGraphicsScore() {
        return graphicsScore;
    }

    public Integer getAudioScore() {
        return audioScore;
    }

    public Integer getControlsScore() {
        return controlsScore;
    }

    public Integer getFunScore() {
        return funScore;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    /**
     * Returns a list of {@link ReviewDto} based on the given collection of {@link Review}.
     *
     * @param reviews The collection of {@link Review}
     * @return A list of {@link ReviewDto}.
     */
    public static List<ReviewDto> createList(Collection<Review> reviews, UriBuilder uriBuilder) {
        return reviews.stream().map(review -> new ReviewDto(review, uriBuilder.clone())).collect(Collectors.toList());
    }


}
