package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.ResetPasswordToken;
import ar.edu.itba.paw.webapp.model.User;

/**
 * Created by Juan Marcos Bellini on 12/12/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public interface ResetPasswordTokenDao {

    /**
     * Retrieves the {@link ResetPasswordToken} with the given {@code nonce.}
     *
     * @param nonce The nonce the retrieved token has.
     * @return The {@link ResetPasswordToken} with the given {@code nonce}, or {@code null} otherwise.
     */
    ResetPasswordToken findByNonce(long nonce);

    /**
     * Creates a new {@link ResetPasswordToken}, and stores it.
     *
     * @param owner The {@link User} owning the token.
     * @return The created token.
     */
    ResetPasswordToken create(User owner);
}
