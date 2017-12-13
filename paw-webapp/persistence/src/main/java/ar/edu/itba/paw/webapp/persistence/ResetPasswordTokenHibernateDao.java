package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ResetPasswordTokenDao;
import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.ResetPasswordToken;
import ar.edu.itba.paw.webapp.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by Juan Marcos Bellini on 12/12/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ResetPasswordTokenHibernateDao implements ResetPasswordTokenDao {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_ITERATIONS = 15;

    @Override
    public ResetPasswordToken findByNonce(long nonce) {

        TypedQuery<ResetPasswordToken> baseQuery = em.createQuery("FROM reset_password_tokens AS R where R.nonce = :nonce", ResetPasswordToken.class);
        baseQuery.setParameter("nonce", nonce);
        try {
            return baseQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ResetPasswordToken create(User owner) {
        boolean successful = false;
        ResetPasswordToken resetPasswordToken;
        int iterations = 0;
        do{
            resetPasswordToken = new ResetPasswordToken(owner);
            if (findByNonce(resetPasswordToken.getNonce()) != null) successful = true;
            iterations++;
        } while (!successful && iterations < MAX_ITERATIONS);
        if(!successful) return null;
        em.persist(resetPasswordToken);
        return resetPasswordToken;
    }
}
