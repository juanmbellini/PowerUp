package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.GameJerseyController;
import ar.edu.itba.paw.webapp.controller.ReviewJerseyController;
import ar.edu.itba.paw.webapp.controller.ThreadJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableWrapper;

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

    @XmlElement
    private Long likeCount;

    @XmlElement
    private Boolean likedByCurrentUser;

    @XmlElement
    private String likesUrl;


    public ReviewDto() {
        // For Jax-RS
    }

    public ReviewDto(LikeableWrapper<Review> wrapper, UriBuilder baseUri) {
        super(wrapper.getEntity().getId());
        this.userId = wrapper.getEntity().getUser().getId();
        this.username = wrapper.getEntity().getUser().getUsername();
        this.gameId = wrapper.getEntity().getGame().getId();
        this.gameName = wrapper.getEntity().getGame().getName();
        this.gameCoverPictureUrl = wrapper.getEntity().getGame().getCoverPictureUrl();
        this.date = wrapper.getEntity().getDate().toString();
        this.body = wrapper.getEntity().getReview();
        this.storyScore = wrapper.getEntity().getStoryScore();
        this.graphicsScore = wrapper.getEntity().getGraphicsScore();
        this.audioScore = wrapper.getEntity().getAudioScore();
        this.controlsScore = wrapper.getEntity().getControlsScore();
        this.funScore = wrapper.getEntity().getFunScore();

        // Urls
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(String.valueOf(wrapper.getEntity().getUser().getId()))
                .build().toString();
        this.gameUrl = baseUri.clone()
                .path(GameJerseyController.END_POINT)
                .path(String.valueOf(wrapper.getEntity().getGame().getId()))
                .build().toString();
        this.likesUrl = baseUri.clone()
                .path(ReviewJerseyController.END_POINT)
                .path(Long.toString(wrapper.getEntity().getId()))
                .path(ReviewJerseyController.LIKES_END_POINT)
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

    public Long getLikeCount() {
        return likeCount;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    /**
     * Returns a list of {@link ReviewDto} based on the given collection of {@link Review}.
     *
     * @param reviews The collection of {@link Review}
     * @return A list of {@link ReviewDto}.
     */
    public static List<ReviewDto> createList(Collection<LikeableWrapper<Review>> reviews, UriBuilder uriBuilder) {
        return reviews.stream().map(review -> new ReviewDto(review, uriBuilder.clone())).collect(Collectors.toList());
    }


}
