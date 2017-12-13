package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ResetPasswordTokenDao;
import ar.edu.itba.paw.webapp.model.ResetPasswordToken;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;

/**
 * Created by Juan Marcos Bellini on 12/12/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Repository
public class ResetPasswordTokenHibernateDao implements ResetPasswordTokenDao {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_ITERATIONS = 15;

    @Override
    public ResetPasswordToken findByNonce(long nonce) {
        return DaoHelper.findByField(em, ResetPasswordToken.class, "nonce", nonce);
    }

    @Override
    public ResetPasswordToken create(User owner) {
        Assert.notNull(owner, "The owner must not be null");
        int count = 0;
        while (count < MAX_ITERATIONS) {
            final ResetPasswordToken token = new ResetPasswordToken(owner);
            // If no other token exists with the nonce of the generated token, store it and return
            if (findByNonce(token.getNonce()) == null) {
                em.persist(token);
                return token;
            }
            count++;
        }
        throw new RuntimeException(MessageFormat.format("Could not create a secure random " +
                "that does not exists in the system after {0} iterations", MAX_ITERATIONS));
    }
}
