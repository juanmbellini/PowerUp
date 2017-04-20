package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.utilities.Page;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Class implementing methods to build a {@link URI} that can be used for further information in a {@link Response}.
 * <p>
 * Created by Juan Marcos Bellini on 19/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
/* package */ class JerseyControllerHelper {


    /**
     * Returns a {@link Response} to be used in a get collection resource method.
     *
     * @param uriInfo           The object that provides access to the requests information.
     * @param sortingTypeString A string that will be included as value for the "orderBy" parameter
     *                          in the {@link URI}s that may be included in the final {@link Response}.
     * @param sortDirection     A {@link SortDirection} value that will be included as a value for the
     *                          "sortDirection" parameter in the {@link URI}s that may be included
     *                          in the final {@link Response}.
     * @param page              The page containing pagination information.
     * @param otherQueryParams  A map containing more parameters (with the given value) to add to the final
     *                          {@link URI}s that may be included in the final {@link Response}.
     * @param <T>               The type of entity that will be included in the body of the {@link Response}.
     * @return The final {@link Response}.
     */
    static /* package */ <T, R> Response createCollectionGetResponse(UriInfo uriInfo, String sortingTypeString,
                                                                     SortDirection sortDirection, Page<T> page,
                                                                     Function<Page<T>, GenericEntity<List<R>>>
                                                                             pageToListFunction,
                                                                     Map<String, Object> otherQueryParams) {
        return createCollectionGetResponse(pageToListFunction.apply(page),
                uriInfo, sortingTypeString, sortDirection, page, otherQueryParams);
    }


    /**
     * Returns a {@link Response} to be used in a get collection resource method.
     *
     * @param listGenericEntity A {@link GenericEntity} of {@link List} containing the objects of type {@code T}
     *                          to be included in the response body.
     * @param uriInfo           The object that provides access to the requests information.
     * @param sortingTypeString A string that will be included as value for the "orderBy" parameter
     *                          in the {@link URI}s that may be included in the final {@link Response}.
     * @param sortDirection     A {@link SortDirection} value that will be included as a value for the
     *                          "sortDirection" parameter in the {@link URI}s that may be included
     *                          in the final {@link Response}.
     * @param page              The page containing pagination information.
     * @param otherQueryParams  A map containing more parameters (with the given value) to add to the final
     *                          {@link URI}s that may be included in the final {@link Response}.
     * @param <T>               The type of entity that will be included in the body of the {@link Response}.
     * @return The final {@link Response}.
     */
    static /* package */ <T> Response createCollectionGetResponse(GenericEntity<List<T>> listGenericEntity,
                                                                  UriInfo uriInfo, String sortingTypeString,
                                                                  SortDirection sortDirection, Page page,
                                                                  Map<String, Object> otherQueryParams) {
        return createCollectionGetResponse(listGenericEntity, uriInfo, sortingTypeString, sortDirection,
                page.getTotalPages(), page.getAmountOfElements(), page.getOverAllAmountOfElements(),
                page.getPageNumber(), page.getPageSize(), otherQueryParams);
    }


    /**
     * Returns a {@link Response} to be used in a get collection resource method.
     *
     * @param genericEntityList       A {@link GenericEntity} of {@link List} containing the objects of type {@code T}
     *                                to be included in the response body.
     * @param uriInfo                 The object that provides access to the requests information.
     * @param sortingTypeString       A string that will be included as value for the "orderBy" parameter
     *                                in the {@link URI}s that may be included in the final {@link Response}.
     * @param sortDirection           A {@link SortDirection} value that will be included as a value for the
     *                                "sortDirection" parameter in the {@link URI}s that may be included
     *                                in the final {@link Response}.
     * @param totalPages              The amount of pages that this collection is slitted into.
     * @param amountOfElements        The amount of elements being returned in the {@link Response}.
     * @param overallAmountOfElements The overall amount of elements this collection has.
     * @param pageNumber              The page number (i.e actual page). This value is used in {@link URI}s
     *                                that may be included in the final {@link Response} in the "pageNumber" parameter.
     * @param pageSize                The page size. This value is used un {@link URI}s that may be included
     *                                in the final {@link Response} in the "pageSize" parameter.
     * @param otherQueryParams        A map containing more parameters (with the given value) to add to the final
     *                                {@link URI}s that may be included in the final {@link Response}.
     * @param <T>                     The type of entity that will be included in the body of the {@link Response}.
     * @return The final {@link Response}.
     */
    static /* package */ <T> Response createCollectionGetResponse(GenericEntity<List<T>> genericEntityList,
                                                                  UriInfo uriInfo, String sortingTypeString,
                                                                  SortDirection sortDirection, int totalPages,
                                                                  int amountOfElements, long overallAmountOfElements,
                                                                  int pageNumber, int pageSize,
                                                                  Map<String, Object> otherQueryParams) {
        return addPaginationHeaders(Response.ok(genericEntityList), totalPages, amountOfElements,
                overallAmountOfElements, pageNumber, pageSize, new PaginationUriContainer(uriInfo,
                        sortingTypeString, sortDirection, pageSize, pageNumber, totalPages, otherQueryParams))
                .build();
    }


    /**
     * Getters a {@link ResponseBuilder} and adds pagination headers to it.
     *
     * @param builder                 The {@link ResponseBuilder}
     * @param totalPages              The amount of pages.
     * @param amountOfElements        The amount of elements in the actual page.
     * @param overallAmountOfElements The amount of elements in all pages.
     * @param pageNumber              The page number (i.e actual page)
     * @param pageSize                The page size.
     * @param container               The container class with all {@link URI}s.
     * @return The {@link ResponseBuilder} with the added headers.
     */
    static /* package */ ResponseBuilder addPaginationHeaders(ResponseBuilder builder, int totalPages,
                                                              int amountOfElements, long overallAmountOfElements,
                                                              int pageNumber, int pageSize,
                                                              PaginationUriContainer container) {
        return addPaginationHeaders(builder, totalPages, amountOfElements, overallAmountOfElements,
                pageNumber, pageSize, container.getPrevPage(), container.getNextPage(), container.getFirstPage(),
                container.getLastPage());
    }


    /**
     * Getters a {@link ResponseBuilder} and adds pagination headers to it.
     *
     * @param builder                 The {@link ResponseBuilder}
     * @param totalPages              The amount of pages.
     * @param amountOfElements        The amount of elements in the actual page.
     * @param overallAmountOfElements The amount of elements in all pages.
     * @param pageNumber              The page number (i.e actual page)
     * @param pageSize                THe page size.
     * @param prevPage                The previous page {@link URI}.
     * @param nextPage                The next page {@link URI}.
     * @param firstPage               The first page {@link URI}.
     * @param lastPage                The last page {@link URI}.
     * @return The {@link ResponseBuilder} with the added headers.
     */
    static /* package */ ResponseBuilder addPaginationHeaders(ResponseBuilder builder, int totalPages,
                                                              int amountOfElements, long overallAmountOfElements,
                                                              int pageNumber, int pageSize,
                                                              URI prevPage, URI nextPage, URI firstPage, URI lastPage) {
        return builder.header("X-Total-Pages", totalPages)
                .header("X-Amount-Of-Elements", amountOfElements)
                .header("X-Overall-Amount-Of-Elements", overallAmountOfElements)
                .header("X-Page-Number", pageNumber)
                .header("X-Page-Size", pageSize)
                .header("X-Prev-Page-Url", prevPage == null ? "" : prevPage.toString())
                .header("X-Next-Page-Url", nextPage == null ? "" : nextPage.toString())
                .header("X-First-Page-Url", firstPage.toString())
                .header("X-Last-Page-Url", lastPage.toString());
    }

    /**
     * Creates a {@link URI} that can be used for further information (e.g pagination links) in a Response.
     * This method allows adding more query params in the final {@link URI}.
     *
     * @param uriInfo           The object that provides access to the requests information.
     * @param sortingTypeString A string that will be included as value for the "orderBy" parameter.
     * @param sortDirection     A {@link SortDirection} value for the "sortDirection" parameter.
     * @param pageSize          The value for the "pageSize" parameter.
     * @param pageNumber        The value for the "pageSize" parameter.
     * @param otherQueryParams  A map containing more parameters (with the given value) to add to the final {@link URI}.
     * @return The created URI.
     * @throws IllegalArgumentException If the {@code otherQueryParams} map contains a null param or value.
     */
    static /* package */ URI createCollectionResponseUri(final UriInfo uriInfo, final String sortingTypeString,
                                                         final SortDirection sortDirection,
                                                         final Integer pageSize, final Integer pageNumber,
                                                         Map<String, Object> otherQueryParams)
            throws IllegalArgumentException {
        final UriBuilder uriBuilder = createCollectionResponseUri(uriInfo, sortingTypeString,
                sortDirection, pageSize, pageNumber);
        otherQueryParams.forEach(uriBuilder::queryParam);
        return uriBuilder.build();
    }

    /**
     * Creates a {@link UriBuilder} from the given {@link UriInfo} base uri ({@link UriInfo#getBaseUriBuilder()}),
     * and adds to it the information for the "orderBy", "sortDirection", "pageSize" and "pageNumber" parameters.
     *
     * @param uriInfo           The object that provides access to the requests information.
     * @param sortingTypeString A string that will be included as value for the "orderBy" parameter.
     * @param sortDirection     A {@link SortDirection} value for the "sortDirection" parameter.
     * @param pageSize          The value for the "pageSize" parameter.
     * @param pageNumber        The value for the "pageSize" parameter.
     * @return The {@link UriBuilder} with those parameters added as query params.
     */
    static /* package */ UriBuilder createCollectionResponseUri(final UriInfo uriInfo, final String sortingTypeString,
                                                                final SortDirection sortDirection,
                                                                final Integer pageSize, final Integer pageNumber) {
        return createCollectionResponseUri(uriInfo.getAbsolutePathBuilder(), sortingTypeString,
                sortDirection, pageSize, pageNumber);

    }

    /**
     * Takes an {@link UriBuilder} and adds to it the information for the "orderBy", "sortDirection", "pageSize" and "pageNumber" parameters.
     *
     * @param uriBuilder        The starting {@link UriBuilder}.
     * @param sortingTypeString A string that will be included as value for the "orderBy" parameter.
     * @param sortDirection     A {@link SortDirection} value for the "sortDirection" parameter.
     * @param pageSize          The value for the "pageSize" parameter.
     * @param pageNumber        The value for the "pageSize" parameter.
     * @return The {@link UriBuilder} with those parameters added as query params.
     */
    static /* package */ UriBuilder createCollectionResponseUri(final UriBuilder uriBuilder,
                                                                final String sortingTypeString,
                                                                final SortDirection sortDirection,
                                                                final Integer pageSize, final Integer pageNumber) {
        return uriBuilder
                .queryParam("orderBy", sortingTypeString == null ? "" : sortingTypeString)
                .queryParam("sortDirection", sortDirection == null ? "" : sortDirection.toString().toLowerCase())
                .queryParam("pageSize", pageSize == null ? "" : pageSize)
                .queryParam("pageNumber", pageNumber == null ? "" : pageNumber);
    }


    /**
     * Returns a {@link ParameterMapBuilder}.
     *
     * @return A new {@link ParameterMapBuilder}.
     */
    static /* package */ ParameterMapBuilder getParameterMapBuilder() {
        return new ParameterMapBuilder();
    }


    /**
     * Class encapsulating the four links used for pagination (i.e previous page, next page, first page and last page).
     */
    /* package */ static final class PaginationUriContainer {

        /**
         * The previous page URI.
         */
        private final URI prevPage;

        /**
         * The next page URI.
         */
        private final URI nextPage;

        /**
         * The first page URI.
         */
        private final URI firstPage;

        /**
         * The last page URI.
         */
        private final URI lastPage;


        /**
         * Constructor.
         *
         * @param uriInfo          The object that provides access to the requests information.
         * @param sortDirection    A {@link SortDirection} value for the "sortDirection" parameter.
         * @param pageSize         The page size (will be included as the "pageSize" parameter value).
         * @param pageNumber       The page number (i.e the actual page).
         *                         Will be included as the "pageNumber" parameter value
         * @param totalPages       The amount of pages.
         * @param otherQueryParams A map containing more parameters (with the given value)
         *                         to add to the final {@link URI}.
         */
        PaginationUriContainer(UriInfo uriInfo, String sortingType, SortDirection sortDirection,
                               int pageSize, int pageNumber, int totalPages,
                               Map<String, Object> otherQueryParams) {

            prevPage = pageNumber == 1 ? null :
                    createCollectionResponseUri(uriInfo, sortingType, sortDirection, pageSize, pageNumber - 1,
                            otherQueryParams);
            nextPage = pageNumber == totalPages ? null :
                    createCollectionResponseUri(uriInfo, sortingType, sortDirection, pageSize, pageNumber + 1,
                            otherQueryParams);

            firstPage = createCollectionResponseUri(uriInfo, sortingType, sortDirection, pageSize, 1, otherQueryParams);

            lastPage = createCollectionResponseUri(uriInfo, sortingType, sortDirection, pageSize,
                    totalPages, otherQueryParams);
        }


        /**
         * Previous page {@link URI} getter.
         *
         * @return The previous page {@link URI}, or {@code null} if there is no previous page
         * (i.e the actual page is the first one).
         */
        public URI getPrevPage() {
            return prevPage;
        }

        /**
         * Next page {@link URI} getter.
         *
         * @return The next page {@link URI}, or {@code null} if there is no next page
         * (i.e the actual page is the last one).
         */
        public URI getNextPage() {
            return nextPage;
        }

        /**
         * First page {@link URI} getter.
         *
         * @return The first page {@link URI}.
         */
        public URI getFirstPage() {
            return firstPage;
        }

        /**
         * Last page {@link URI} getter.
         *
         * @return The last page {@link URI}.
         */
        public URI getLastPage() {
            return lastPage;
        }
    }


    /**
     * Class that contains logic to create a map with {@link String} as key object and {@link String} as value object,
     * than can be used as an "otherQueryParams" container to be used in the
     * {@link JerseyControllerHelper#createCollectionResponseUri(UriInfo, String, SortDirection, Integer, Integer, Map)}
     * method.
     */
    /* package */ static final class ParameterMapBuilder {

        private final Map<String, Object> parameters;


        /**
         * Private constructor.
         */
        private ParameterMapBuilder() {
            this.parameters = new HashMap<>();
        }


        /**
         * Clears the builder.
         *
         * @return this (for method chaining).
         */
        /* package */ ParameterMapBuilder clear() {
            parameters.clear();
            return this;
        }

        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, String value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }

        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Long value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }


        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Integer value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }

        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Double value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }

        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Float value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }


        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Boolean value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }

        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Byte value)
                throws IllegalArgumentException {
            return addParameter(parameter, (Object) value);
        }


        /**
         * Adds the given {@code parameter}, associated with the given {@code value}.
         * If the given {@code value} is {@code null}, an empty string is added as the value.
         *
         * @param parameter The parameter.
         * @param value     The value.
         * @return this (for method chaining).
         * @throws IllegalArgumentException if the given {@code parameter} is null.
         */
        /* package */ ParameterMapBuilder addParameter(String parameter, Object value)
                throws IllegalArgumentException {
            if (parameter == null) {
                throw new IllegalArgumentException();
            }
            parameters.put(parameter, value == null ? "" : value);
            return this;
        }


        /**
         * Builds the map.
         *
         * @return The map containing the parameters and the values.
         */
        /* package */ Map<String, Object> build() {
            return parameters;
        }

    }
}
