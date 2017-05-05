package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Thread;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 19/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thread")
public class ThreadDto extends EntityDto {


    @XmlElement
    private String title;


    @XmlElement
    private String body;

    @XmlElement
    private Long creatorId;

    @XmlElement
    private String creatorUsername;

    @XmlElement
    private String creatorUrl;


    public ThreadDto() {
        // For Jax-RS
    }


    public ThreadDto(Thread thread) {
        super(thread.getId());
        this.title = thread.getTitle();
        this.body = thread.getInitialComment();
        this.creatorId = thread.getCreator().getId();
        this.creatorUsername = thread.getCreator().getUsername();
    }


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getCreatorUrl() {
        return creatorUrl;
    }

    /**
     * Returns a list of {@link ThreadDto} based on the given collection of {@link Thread}.
     *
     * @param threads The collection of {@link Thread}
     * @return A list of {@link ThreadDto}.
     */
    public static List<ThreadDto> createList(Collection<Thread> threads) {
        return threads.stream().map(ThreadDto::new).collect(Collectors.toList());
    }
}
