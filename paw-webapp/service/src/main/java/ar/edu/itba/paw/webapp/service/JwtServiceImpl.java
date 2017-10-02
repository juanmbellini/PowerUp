package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.JwtDao;
import ar.edu.itba.paw.webapp.interfaces.JwtService;
import ar.edu.itba.paw.webapp.model.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class JwtServiceImpl implements JwtService {

    private final JwtDao jwtDao;

    @Autowired
    public JwtServiceImpl(JwtDao jwtDao) {
        this.jwtDao = jwtDao;
    }

    @Override
    public List<Jwt> all() {
        return jwtDao.all();
    }

    @Override
    public Jwt findById(long id) {
        return jwtDao.findById(id);
    }

    @Override
    public Jwt find(String token) {
        return jwtDao.find(token);
    }

    @Override
    public Jwt blacklist(String tokenString, Calendar expirationDate) {
        return jwtDao.blacklist(tokenString, expirationDate);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return jwtDao.isBlacklisted(token);
    }

    @Override
    public boolean isExpired(String token) {
        return jwtDao.isExpired(token);
    }

    @Override
    public void delete(Jwt token) {
        jwtDao.delete(token);
    }
}
