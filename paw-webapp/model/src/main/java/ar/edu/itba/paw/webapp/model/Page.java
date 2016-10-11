package ar.edu.itba.paw.webapp.model;

import java.util.Collection;

/**
 * Class representing a search result page.
 * <p>
 * In order to use this class, {@code totalPages}, {@code pageNumber}, and {@code pageSize}
 * must be set with values different than {@code 0} before setting data.
 * <p>
 * Created by Juan Marcos Bellini on 11/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class Page<T> {


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
     * Data getter.
     *
     * @return The data in this page.
     */
    public Collection<T> getData() {
        return data;
    }

    /**
     * Total pages setter.
     *
     * @param totalPages The total amount of pages there are in the search done to obtain the data of in this page.
     */
    public void setTotalPages(int totalPages) {
        if (totalPages < 0) {
            throw new IllegalArgumentException();
        }
        this.totalPages = totalPages;
    }

    /**
     * Page number setter.
     *
     * @param pageNumber This page's number.
     */
    public void setPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            throw new IllegalArgumentException();
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
            throw new IllegalArgumentException();
        }
        this.pageSize = pageSize;
    }

    /**
     * Data setter.
     *
     * @param data The data to be added into this page.
     */
    public void setData(Collection<T> data) {
        if (pageSize == 0 || pageNumber == 0 || totalPages == 0) {
            throw new IllegalStateException();
        }
        if (data.size() > pageSize) {
            throw new IllegalArgumentException();
        }
        this.data = data;
    }
}
