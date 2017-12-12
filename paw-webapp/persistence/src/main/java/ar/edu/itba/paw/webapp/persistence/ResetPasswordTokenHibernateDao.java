package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ResetPasswordTokenDao;
import ar.edu.itba.paw.webapp.model.ResetPasswordToken;
import ar.edu.itba.paw.webapp.model.User;

/**
 * Created by Juan Marcos Bellini on 12/12/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ResetPasswordTokenHibernateDao implements ResetPasswordTokenDao {

    @Override
    public ResetPasswordToken findByNonce(long nonce) {
        // TODO: implement
        return null;
    }

    @Override
    public ResetPasswordToken create(User owner) {
        // TODO: implement...
        // TODO: when creating the ResetPasswordToken, a unique validation of the nonce must be performed before saving it
        // TODO: however, it's not possible to store two tokens with same nonce because of unique index in db,
        // TODO: but better check to avoid exception, and always finish the task well.
        return null;
    }
}
