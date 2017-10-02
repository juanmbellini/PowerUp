package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.FilterCategory;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 22/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlRootElement(name = "filterCategory")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterCategoryDto {

    @XmlElement
    private String category;

    @XmlElement
    private String valuesUrl;

    public FilterCategoryDto() {
        // Default constructor
    }

    public FilterCategoryDto(FilterCategory filterCategory, UriInfo uriInfo) {
        this.category = filterCategory.name();
        this.valuesUrl = uriInfo.getAbsolutePathBuilder().path(filterCategory.name()).build().toString();
    }


    /**
     * Returns a list of {@link FilterCategoryDto}
     *
     * @param uriInfo The uri information to create each filter's url.
     * @return A list of {@link FilterCategoryDto}.
     */
    public static List<FilterCategoryDto> createList(UriInfo uriInfo) {
        return Arrays.stream(FilterCategory.values()).map(each -> new FilterCategoryDto(each, uriInfo))
                .collect(Collectors.toList());
    }

}
