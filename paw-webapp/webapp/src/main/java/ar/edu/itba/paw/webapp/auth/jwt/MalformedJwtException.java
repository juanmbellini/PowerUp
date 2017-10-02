package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * @see <a href="https://gitlab.com/palmapps/jwt-spring-security-demo/blob/7a05e9cd77af737e5b51197f8fdf88f20d022d29/src/main/java/nl/palmapps/myawesomeproject/security/exception/JwtTokenMalformedException.java">Source</a>
 */
public class MalformedJwtException extends AuthenticationException {

    public MalformedJwtException (String msg) {
        super(msg);
    }
}
