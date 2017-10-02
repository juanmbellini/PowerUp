package ar.edu.itba.paw.webapp.exceptions;

/**
 * This is a special type of exception that can be thrown before creating the page.
 * For example, if the same statement contains the page creation and the database query, this exception can be thrown
 * before executing the statement, avoiding the query if the page values are not correct.
 * <p>
 * Created by Juan Marcos Bellini on 5/5/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class NumberOfPageBiggerThanTotalAmountException extends IllegalPageException {


    public NumberOfPageBiggerThanTotalAmountException() {
        super();
    }

    public NumberOfPageBiggerThanTotalAmountException(String msg) {
        super(msg);
    }
}
