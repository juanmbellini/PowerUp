package ar.edu.itba.paw.webapp.utilities;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;

import java.util.Collection;
import java.util.Collections;

/**
 * Class representing a search result page.
 * <p>
 * In order to use this class, {@code totalPages}, {@code pageNumber}, and {@code pageSize}
 * must be set with values different than {@code 0} before setting data.
 * <p>
 * Created by Juan Marcos Bellini on 11/10/16.
 */
public class Page<T> {

    /**
     * Contains an empty page, configured to have no elements, and only one page.
     */
    private static final Page EMPTY_PAGE = new Page<>();


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


    // TODO: remove setters moving their logic to the builder


    /**
     * Total pages setter.
     *
     * @param totalPages The total amount of pages there are in the search done to obtain the data of in this page.
     */
    public void setTotalPages(int totalPages) {
        if (totalPages < 0) {
            throw new IllegalPageException();
        }
        this.totalPages = totalPages;
    }

    /**
     * Page number setter.
     * <p>
     * If page number is greater than {@code totalPages}, an {@IllegalPageException} is thrown.
     * Note: {@code totalPages} is set to {@code 0} initially.
     * </p>
     *
     * @param pageNumber This page's number.
     */
    public void setPageNumber(int pageNumber) {
        if (pageNumber <= 0 || pageNumber > totalPages) {
            throw new IllegalPageException("The page number is bigger than the total amount of pages.");
        }
        this.pageNumber = pageNumber;
    }

    /**
     * Page size setter.
     *
     * @param pageSize This page's size.
     */
    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalPageException();
        }
        this.pageSize = pageSize;
    }

    /**
     * Overall amount of elements setter
     *
     * @param overAllAmountOfElements The number of elements in all the pages
     */
    public void setOverAllAmountOfElements(long overAllAmountOfElements) {
        if (overAllAmountOfElements > pageSize * totalPages) {
            throw new IllegalPageException();
        }
        this.overAllAmountOfElements = overAllAmountOfElements;
    }

    /**
     * Data setter.
     * <p>
     * Note: If the total amount of data in the collection is smaller than {@code pageSize},
     * then this field is overwritten.
     * </p>
     *
     * @param data The data to be added into this page.
     */
    public void setData(Collection<T> data) {
        if (pageSize == 0 || pageNumber == 0 || totalPages == 0) {
            throw new IllegalPageException("Illegal state.");
        }
        if (data.size() > pageSize) {
            throw new IllegalPageException("Page size is smaller than the amount of data in the collection.");
        }
        this.data = data;
//        this.pageSize = data.size();
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
        EMPTY_PAGE.overAllAmountOfElements = 0;
        EMPTY_PAGE.pageSize = 0;
        EMPTY_PAGE.pageNumber = 1;
        EMPTY_PAGE.totalPages = 1;
        EMPTY_PAGE.data = Collections.emptyList();

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
        Page<T> page = new Page<>();
        page.setTotalPages(1);
        page.setPageSize(1);
        page.setPageNumber(1);
        page.setOverAllAmountOfElements(1);
        page.setData(Collections.singleton(element));
        return page;
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
            Page<T> page = new Page<>();
            page.setTotalPages(totalPages);
            page.setPageNumber(pageNumber);
            page.setPageSize(pageSize);
            page.setOverAllAmountOfElements(overAllAmountOfElements);
            page.setData(data);
            return page;
        }


    }

}
