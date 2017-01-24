package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.FilterCategory;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 20/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterDto {

    @XmlElement(required = true)
    private FilterCategory filterCategory;

    @XmlElement(required = true)
    private List<String> values;


    public FilterDto() {
        // Default constructor
    }

    public FilterDto(FilterCategory filterCategory, List<String> values) {
        this.filterCategory = filterCategory;
        this.values = values;
    }


    public FilterCategory getFilterCategory() {
        return filterCategory;
    }

    public List<String> getValues() {
        return values;
    }

    /**
     * Returns a list of {@link FilterDto} based on the given map, using the keys as {@code filterCategory},
     * and values as {@code values}.
     *
     * @param filters The map of {@link FilterCategory}
     * @return A list of {@link FilterDto}.
     */
    public static List<FilterDto> createList(Map<FilterCategory, List<String>> filters) {
        return filters.entrySet().stream().map(each -> new FilterDto(each.getKey(), each.getValue())).collect(Collectors.toList());
    }

}
