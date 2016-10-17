package ar.edu.itba.paw.webapp.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum representing granted user authorities.
 */
public enum Authority implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
