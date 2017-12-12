package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 20/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterValueDto {


    @XmlElement(required = true)
    private String value;


    public FilterValueDto() {
        // Default constructor
    }

    public FilterValueDto(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    /**
     * Returns a list of {@link FilterValueDto} based on the given map, using the keys as {@code filterCategory},
     * and values as {@code values}.
     *
     * @return A list of {@link FilterValueDto}.
     */
    public static List<FilterValueDto> createList(Collection<String> values) {
        return values.stream().map(FilterValueDto::new).collect(Collectors.toList());
    }
}
