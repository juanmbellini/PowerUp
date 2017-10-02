package ar.edu.itba.paw.webapp.api_errors;

/**
 * Abstract representation of an error.
 * Any error returned by the API will contain an "errorKind" field telling if it is a Client Side ApiError, or a
 * Server Side ApiError.
 *
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public abstract class ApiError {

	private String errorKind;


	public ApiError(String errorKind) {
		this.errorKind = errorKind;
	}

	public String getErrorKind() {
		return errorKind;
	}
}
