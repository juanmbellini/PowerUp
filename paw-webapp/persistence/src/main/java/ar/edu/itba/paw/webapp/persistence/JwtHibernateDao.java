package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.JwtDao;
import ar.edu.itba.paw.webapp.model.Jwt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;

@Repository
public class JwtHibernateDao implements JwtDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Jwt findById(long id) {
        return DaoHelper.findSingle(em, Jwt.class, id);
    }

    @Override
    public Jwt find(String token) {
        return DaoHelper.findSingleWithConditions(em, Jwt.class, "FROM Jwt AS J WHERE J.token = ?1", token);
    }

    @Override
    public Jwt blacklist(String tokenString, Calendar expirationDate) {
        final Jwt token = new Jwt(tokenString, expirationDate);
        em.persist(token);
        return token;
    }

    @Override
    public boolean isBlacklisted(String token) {
        return find(token) != null;
    }

    @Override
    public boolean isExpired(String tokenString) throws NoSuchEntityException {
        Jwt token = find(tokenString);
        if(token == null) {
            throw new NoSuchEntityException(Jwt.class, tokenString);
        }
        return token.getValidUntil().before(Calendar.getInstance());
    }
}
