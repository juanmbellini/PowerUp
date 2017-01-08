package ar.edu.itba.paw.webapp.errors;

/**
 * Abstract representation of an error.
 * Any error returned by the API will contain an "errorKind" field telling if it is a Client Side Error, or a
 * Server Side Error.
 *
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public abstract class Error {

	private String errorKind;


	public Error(String errorKind) {
		this.errorKind = errorKind;
	}

	public String getErrorKind() {
		return errorKind;
	}
}
