package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableEntityWrapper;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 7/7/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment")
public class CommentDto extends EntityDto {

    @XmlElement
    private Long threadId;

    @XmlElement
    private String body;

    @XmlElement
    private String createdAt;

    @XmlElement
    private Long likeCount;

    @XmlElement
    private ThreadDto.CreatorDto commenter;


    public CommentDto() {
        // For Jax-RS
    }

    public CommentDto(LikeableEntityWrapper<Comment> commentWrapper, UriBuilder baseUri) {
        super(commentWrapper.getEntity().getId());
        this.body = commentWrapper.getEntity().getBody();
        this.createdAt = LocalDateTime.ofInstant(commentWrapper.getEntity().getCreatedAt().toInstant(),
                ZoneId.systemDefault()).toString();
        this.likeCount = commentWrapper.getLikeCount();
        this.commenter = new ThreadDto.CreatorDto(commentWrapper.getEntity().getCommenter(), baseUri);
        this.threadId = commentWrapper.getEntity().getThread().getId();
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

    /**
     * Returns a list of {@link CommentDto} based on the given collection of {@link Comment}.
     *
     * @param comments The collection of {@link Comment}
     * @return A list of {@link CommentDto}.
     */
    public static List<CommentDto> createList(Collection<LikeableEntityWrapper<Comment>> comments,
                                              UriBuilder uriBuilder) {
        return comments.stream().map(wrapper -> new CommentDto(wrapper, uriBuilder.clone()))
                .collect(Collectors.toList());
    }

}
