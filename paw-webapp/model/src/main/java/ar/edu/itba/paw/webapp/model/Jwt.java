package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * JSON Web Token model. Used for authentication.
 */
@Entity
@Table(name = "jwt_blacklist")
public class Jwt {

    @Id
    @SequenceGenerator(name = "jwt_seq", sequenceName = "jwt_blacklist_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_seq")
    private long id;

    private String token;

    @Column(name = "valid_until")
    private Calendar validUntil;

    /*package*/ Jwt() {
        //For Hibernate
    }

    public Jwt(String tokenString, Calendar validUntil) {
        this.token = tokenString;
        this.validUntil = validUntil;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Calendar getValidUntil() {
        return validUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jwt jwt = (Jwt) o;

        return id == jwt.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
