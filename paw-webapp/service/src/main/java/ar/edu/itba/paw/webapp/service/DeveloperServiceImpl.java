package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.DeveloperDao;
import ar.edu.itba.paw.webapp.interfaces.DeveloperService;
import ar.edu.itba.paw.webapp.model.Developer;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
public class DeveloperServiceImpl implements DeveloperService {

    @Autowired
    private DeveloperDao developerDao;

    @Override
    public Set<Developer> all() {
        return developerDao.all();
    }

    @Override
    public Developer findById(long id) {
        return developerDao.findById(id);
    }

    @Override
    public Developer findByName(String name) {
        return developerDao.findByName(name);
    }

    @Override
    public Set<Game> gamesDevelopedBy(Developer p) {
        return developerDao.gamesDevelopedBy(p);
    }
}
