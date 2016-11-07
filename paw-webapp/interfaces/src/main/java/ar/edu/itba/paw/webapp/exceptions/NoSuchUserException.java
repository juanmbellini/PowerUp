package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.model.User;

/**
 * Created by juan_ on 06-Nov-16.
 */
public class NoSuchUserException extends NoSuchEntityException {
    public NoSuchUserException() {
        super("No such user");
    }

    public NoSuchUserException(long userId) {
        super(User.class, userId);
    }

    public NoSuchUserException(String username) {
        super("No User with username " + username);
    }
}
