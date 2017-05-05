package ar.edu.itba.paw.webapp.utilities;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class representing a search result page.
 * <p>
 * It implements the Builder pattern in order to create consistent Pages.
 * <p>
 * Created by Juan Marcos Bellini on 11/10/16.
 */
public class Page<T> {

    /**
     * Contains an empty page, configured to have no elements, and only one page.
     */
    private static final Page EMPTY_PAGE = new Page<>(0, 0, 1, 1, Collections.unmodifiableList(new LinkedList<>()));


    /**
     * The total amount of pages there are in the search done to obtain the data included in this page
     */
    private int totalPages = 0;
    /**
     * This page number
     */
    private int pageNumber = 0;
    /**
     * The total amount of elements there are in this page
     */
    private int pageSize = 0;
    /**
     * The total amount of elements in all the pages
     */
    private long overAllAmountOfElements = 0;
    /**
     * The data included in this page
     */
    private Collection<T> data = null;


    /**
     * Private constructor used for the {@link Builder}.
     *
     * @param totalPages              The total amount of pages.
     * @param pageNumber              The page number.
     * @param pageSize                The page size.
     * @param overAllAmountOfElements The total amount of elements in all pages.
     * @param data                    The data in this page.
     */
    public Page(int totalPages, int pageNumber, int pageSize, long overAllAmountOfElements, Collection<T> data) {

        if (totalPages <= 0) {
            throw new IllegalPageException("The total amount of pages must be a positive number.");
        }
        if (pageNumber <= 0 || pageNumber > totalPages) {
            throw new IllegalPageException("The page number is bigger than the total amount of pages.");
        }
        if (pageSize < 0) {
            throw new IllegalPageException("The page size can't be negative.");
        }
        if (overAllAmountOfElements > pageSize * totalPages) {
            throw new IllegalPageException("The overall amount of data can't be greater than the overall space.");
        }
        if (data.size() > pageSize) {
            throw new IllegalPageException("Page size is smaller than the amount of data in the collection.");
        }

        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.overAllAmountOfElements = overAllAmountOfElements;
        this.data = data;
    }


    /**
     * Total pages getter.
     *
     * @return The total amount of pages.
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Page number getter.
     *
     * @return The number of this page.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Page size getter.
     *
     * @return The size of this page.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns how many elements are there in this page.
     *
     * @return The amount of elements in this page.
     */
    public int getAmountOfElements() {
        return data == null ? 0 : data.size();
    }

    /**
     * Overall Amount of Elements getter
     *
     * @return The overall amount of elements in all the pages
     */
    public long getOverAllAmountOfElements() {
        return overAllAmountOfElements;
    }

    /**
     * Data getter.
     *
     * @return The data in this page.
     */
    public Collection<T> getData() {
        return data;
    }


    public boolean isEmpty() {
        return pageSize == 0;
    }

    /**
     * Returns an empty page (i.e. with no data, but with page number and total pages equals to 1)
     *
     * @param <T> The type of data that the page would store
     * @return An empty page.
     */
    public static <T> Page<T> emptyPage() {
        //noinspection unchecked
        return (Page<T>) EMPTY_PAGE;
    }

    /**
     * Returns a single-element page. Convenience method.
     *
     * @param element The single element in the page.
     * @param <T>     The element type.
     * @return A page with 1 element.
     */
    public static <T> Page<T> singleElementPage(T element) {
        return new Page<>(1, 1, 1, 1, Stream.of(element).collect(Collectors.toList()));
    }

    /**
     * Page builder.
     *
     * @param <T> The type of data the page will store.
     */
    public static class Builder<T> {


        private int totalPages = 0;
        private int pageNumber = 0;
        private int pageSize = 0;
        private long overAllAmountOfElements = 0;
        private Collection<T> data = null;

        private Page<T> page;


        /**
         * Sets the total amount of pages.
         *
         * @param totalPages The total amount of pages.
         * @return This builder.
         */
        public Builder<T> setTotalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        /**
         * Sets the page number.
         *
         * @param pageNumber The page number.
         * @return This builder.
         */
        public Builder<T> setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        /**
         * Sets the page size (how many elements the future built page will have).
         *
         * @param pageSize The page size.
         * @return This builder.
         */
        public Builder<T> setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * Sets the overall amount of elements (all elements all pages will have)
         *
         * @param overAllAmountOfElements The overall amount of elements.
         * @return This builder.
         */
        public Builder<T> setOverAllAmountOfElements(long overAllAmountOfElements) {
            this.overAllAmountOfElements = overAllAmountOfElements;
            return this;
        }

        /**
         * Sets the data of this page.
         *
         * @param data The data of this page.
         * @return This builder.
         */
        public Builder<T> setData(Collection<T> data) {
            this.data = data;
            return this;
        }


        /**
         * Builds the page using the configured values,
         * without throwing an exception because of not setting those values in the correct order.
         *
         * @return The built page.
         * @throws IllegalPageException If some value is wrong or inconsistent.
         */
        public Page<T> build() throws IllegalPageException {
            return new Page<>(totalPages, pageNumber, pageSize, overAllAmountOfElements, data);
        }


    }


}
