package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "")
public class EntityDto {

    private Long id;


    public EntityDto(Long id) {
        this.id = id;
    }

    public EntityDto() {
        // For Jax-RS
    }


    /*
     * Having the @XmlElement annotation in th
     */

    /**
     * Returns the entity's id.
     *
     * @return The entity's id.
     * @implNote This method is annotated with the {@link XmlElement} annotation in order to let SubClasses
     * override the property when overriding the method. For example, if the name must be changed,
     * the overrider method can include a {@link JsonProperty} with {@code value} set to the new name.
     */
    @XmlElement(required = true)
    public Long getId() {
        return id;
    }
}
