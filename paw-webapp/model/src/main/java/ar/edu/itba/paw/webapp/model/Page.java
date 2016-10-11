package ar.edu.itba.paw.webapp.model;

import java.util.Collection;

/**
 * Class representing a search result page
 * <p>
 * Created by Juan Marcos Bellini on 11/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class Page<T> {


    /**
     * The total amount of pages there are in the search done to obtain the data included in this page
     */
    private int totalPages;
    /**
     * This page number
     */
    private int pageNumber;
    /**
     * The total amount of elements there are in this page
     */
    private int pageSize;
    /**
     * The data included in this page
     */
    private Collection<T> data;


    /**
     * Constructor.
     *
     * @param pageNumber This page number.
     * @param pageSize   This page size (i.e. the amount of elements in this page).
     */
    public Page(int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
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
     * Data setter.
     *
     * @param data The data to be added into this page.
     */
    public void setData(Collection<T> data) {
        if (data.size() > pageSize) {
            throw new IllegalArgumentException();
        }
        this.data = data;
    }
}
