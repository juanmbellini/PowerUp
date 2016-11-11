package ar.edu.itba.paw.webapp.utilities;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;

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
}
