package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.PlatformDao;
import ar.edu.itba.paw.webapp.interfaces.PlatformService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class PlatformServiceImpl implements PlatformService {

    private final PlatformDao platformDao;

    @Autowired
    public PlatformServiceImpl(PlatformDao platformDao) {
        this.platformDao = platformDao;
    }

    @Override
    public List<Platform> all() {
        return platformDao.all();
    }

    @Override
    public Platform findById(long id) {
        return platformDao.findById(id);
    }

    @Override
    public Platform findByName(String name) {
        return platformDao.findByName(name);
    }

    @Override
    public Set<Game> gamesReleasedFor(Platform p) {
        return platformDao.gamesReleasedFor(p);
    }

    @Override
    public LocalDate releaseDateForGame(Game g, Platform p) {
        return platformDao.releaseDateForGame(g, p);
    }
}
