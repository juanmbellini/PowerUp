package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameDao gameDao;

    public GameServiceImpl() {
    }

    @Override
    public Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters) {
        return gameDao.searchGames(name, filters);
    }

    @Override
    public Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Game findById(long id) {
        return gameDao.findById(id);
    }

    public Collection<String> getFiltersByType(FilterCategory filterCategory) {
        return gameDao.getFiltersByType(filterCategory);
    }
}
