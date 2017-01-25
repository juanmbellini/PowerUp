package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.CompanyDao;
import ar.edu.itba.paw.webapp.interfaces.CompanyService;
import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public List<Company> all() {
        return companyDao.all();
    }

    @Override
    public Company findById(long id) {
        return companyDao.findById(id);
    }

    @Override
    public Company findByName(String name) {
        return companyDao.findByName(name);
    }

    @Override
    public Set<Game> gamesDevelopedBy(Company d) {
        return companyDao.gamesDevelopedBy(d);
    }

    @Override
    public Set<Game> gamesPublishedBy(Company p) {
        return companyDao.gamesPublishedBy(p);
    }
}
