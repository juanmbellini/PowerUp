package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.core.AuthenticationException;

public class MissingJwtException extends AuthenticationException {

    public MissingJwtException(String msg) {
        super(msg);
    }
}
