package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameDao gameDao;

    public GameServiceImpl() {}

    @Override
    public List<Game> searchGames(String name, Collection<Filter> filters) {
        return gameDao.searchGames(name, filters);
    }

    @Override
    public Game findById(int id) {
        return gameDao.findById(id);
    }


}
