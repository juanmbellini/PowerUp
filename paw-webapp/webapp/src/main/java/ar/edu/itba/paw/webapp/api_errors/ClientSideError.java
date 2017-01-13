package ar.edu.itba.paw.webapp.api_errors;

/**
 * Abstract representation of a client side error.
 * <p>
 * A client side error is that one caused by the user hitting the API. All Client side errors contain a "groupCode"
 * field which tells what kind of error is being reported, and a message to give some feedback to the consumer.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public abstract class ClientSideError extends ApiError {

    private String message;

    private int groupCode;

    public ClientSideError(String message, int groupCode) {
        super("Client side error");
        this.message = message;
        this.groupCode = groupCode;
    }

    public String getMessage() {
        return message;
    }

    public int getGroupCode() {
        return groupCode;
    }
}
