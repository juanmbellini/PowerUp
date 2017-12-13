package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValidationHelper;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Object representing a token to be used to reset a password.
 */
@Entity
@Table(name = "reset_password_tokens", indexes = {
        @Index(name = "reset_password_tokens_nonce_unique_index", columnList = "nonce", unique = true)
})
public class ResetPasswordToken implements ValidationExceptionThrower {

    private final static long TOKEN_DURATION_IN_MINUTES = 30;

    /**
     * The id of this token.
     */
    @Id
    @SequenceGenerator(name = "reset_password_tokens_seq", sequenceName = "reset_password_tokens_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_password_tokens_seq")
    private long id;

    /**
     * The {@link User} to which this token token belongs to.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User owner;

    /**
     * {@link Instant} at which this token is created.
     */
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Max. {@link Instant} to use this token.
     */
    @Column(name = "expires_at", updatable = false)
    private Instant expiresAt;

    /**
     * A value which will represent this token.
     */
    @Column(name = "nonce", updatable = false)
    private long nonce;

    /* package */ ResetPasswordToken() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param owner The {@link User} to which this token toekn belongs to.
     */
    public ResetPasswordToken(User owner) {
        final List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(owner, errorList, ValueErrorConstants.MISSING_OWNER);
        throwValidationException(errorList);

        this.owner = owner;
        this.createdAt = Instant.now();
        this.expiresAt = this.createdAt.plus(TOKEN_DURATION_IN_MINUTES, ChronoUnit.MINUTES);
        this.nonce = new SecureRandom().nextLong();
    }


    /**
     * @return The id of this token.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The {@link User} to which this token token belongs to.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return {@link Instant} at which this token is created.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * @return Max. {@link Instant} to use this token.
     */
    public Instant getExpiresAt() {
        return expiresAt;
    }

    /**
     * @return A value which will represent this token.
     */
    public long getNonce() {
        return nonce;
    }


    // ================ Equals and Hashcode based on id ================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResetPasswordToken)) return false;

        ResetPasswordToken that = (ResetPasswordToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
