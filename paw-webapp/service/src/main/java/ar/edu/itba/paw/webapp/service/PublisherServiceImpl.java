package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.PublisherDao;
import ar.edu.itba.paw.webapp.interfaces.PublisherService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private PublisherDao PublisherDao;

    @Override
    public Set<Publisher> all() {
        return PublisherDao.all();
    }

    @Override
    public Publisher findById(long id) {
        return PublisherDao.findById(id);
    }

    @Override
    public Publisher findByName(String name) {
        return PublisherDao.findByName(name);
    }

    @Override
    public Set<Game> gamesPublishedBy(Publisher p) {
        return PublisherDao.gamesPublishedBy(p);
    }
}
