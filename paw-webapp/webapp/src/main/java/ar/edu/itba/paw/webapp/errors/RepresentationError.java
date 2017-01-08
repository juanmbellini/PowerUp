package ar.edu.itba.paw.webapp.errors;

/**
 * A representation error is that one that occurs when receiving an entity that cannot be interpreted as a JSON or XML.
 * <p>
 * Created by Juan Marcos Bellini on 20/12/16.
 */
public class RepresentationError extends ClientSideError {

    public RepresentationError() {
        super("Entity Representation Error", 3);
    }
}
