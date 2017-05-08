package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * @see <a href="https://gitlab.com/palmapps/jwt-spring-security-demo/blob/7a05e9cd77af737e5b51197f8fdf88f20d022d29/src/main/java/nl/palmapps/myawesomeproject/security/model/JwtAuthenticationToken.java">source</a>
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String token;

    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
