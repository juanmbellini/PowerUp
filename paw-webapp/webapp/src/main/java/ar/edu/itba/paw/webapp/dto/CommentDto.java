package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.ThreadJerseyController;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableWrapper;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 7/7/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment")
public class CommentDto extends EntityDto {


    @XmlElement
    private String body;

    @XmlElement
    private String createdAt;

    @XmlElement
    private Long inReplyTo;

    @XmlElement
    private String inReplyToUrl;

    @XmlElement
    private Long likeCount;

    @XmlElement
    private Boolean likedByCurrentUser;

    @XmlElement
    private ThreadDto.CreatorDto commenter;

    @XmlElement
    private String repliesUrl;

    @XmlElement
    private String likesUrl;

    @XmlElement
    private Long threadId;

    @XmlElement
    private String threadUrl;


    public CommentDto() {
        // For Jax-RS
    }

    public CommentDto(LikeableWrapper<Comment> wrapper, UriBuilder baseUri) {
        super(wrapper.getEntity().getId());
        this.body = wrapper.getEntity().getBody();
        this.createdAt = LocalDateTime.ofInstant(wrapper.getEntity().getCreatedAt().toInstant(),
                ZoneId.systemDefault()).toString();
        this.inReplyTo = Optional.ofNullable(wrapper.getEntity().getParentComment()).map(Comment::getId).orElse(null);
        this.inReplyToUrl = Optional.ofNullable(wrapper.getEntity().getParentComment())
                .map(comment -> baseUri.clone()
                        .path(ThreadJerseyController.END_POINT)
                        .path(ThreadJerseyController.COMMENTS_END_POINT)
                        .path(Long.toString(comment.getId()))
                        .build().toString())
                .orElse(null);
        this.likeCount = wrapper.getLikeCount();
        this.likedByCurrentUser = wrapper.getLikedByCurrentUser();
        this.commenter = new ThreadDto.CreatorDto(wrapper.getEntity().getCommenter(), baseUri);
        this.repliesUrl = baseUri.clone()
                .path(ThreadJerseyController.END_POINT)
                .path(ThreadJerseyController.COMMENTS_END_POINT)
                .path(Long.toString(wrapper.getEntity().getId()))
                .path(ThreadJerseyController.REPLIES_END_POINT)
                .build().toString();
        this.likesUrl = baseUri.clone()
                .path(ThreadJerseyController.END_POINT)
                .path(ThreadJerseyController.COMMENTS_END_POINT)
                .path(Long.toString(wrapper.getEntity().getId()))
                .path(ThreadJerseyController.LIKES_END_POINT)
                .build().toString();
        this.threadId = wrapper.getEntity().getThread().getId();
        this.threadUrl = baseUri.clone()
                .path(ThreadJerseyController.END_POINT)
                .path(Long.toString(wrapper.getEntity().getThread().getId()))
                .build().toString();
    }

    public Long getThreadId() {
        return threadId;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ThreadDto.CreatorDto getCommenter() {
        return commenter;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public String getThreadUrl() {
        return threadUrl;
    }

    public String getRepliesUrl() {
        return repliesUrl;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public Long getInReplyTo() {
        return inReplyTo;
    }

    public String getInReplyToUrl() {
        return inReplyToUrl;
    }

    /**
     * Returns a list of {@link CommentDto} based on the given collection of {@link Comment}.
     *
     * @param comments The collection of {@link Comment}
     * @return A list of {@link CommentDto}.
     */
    public static List<CommentDto> createList(Collection<LikeableWrapper<Comment>> comments,
                                              UriBuilder uriBuilder) {
        return comments.stream().map(wrapper -> new CommentDto(wrapper, uriBuilder.clone()))
                .collect(Collectors.toList());
    }

}
