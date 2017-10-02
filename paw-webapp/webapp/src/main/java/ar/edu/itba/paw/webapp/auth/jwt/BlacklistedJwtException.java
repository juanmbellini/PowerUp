package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.core.AuthenticationException;

public class BlacklistedJwtException extends AuthenticationException {

    public BlacklistedJwtException(String msg) {
        super(msg);
    }

    public BlacklistedJwtException() {
        this("Attempted authentication with a blacklisted JWT");
    }
}
