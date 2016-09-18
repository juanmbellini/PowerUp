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

@Service
public class GameServiceImpl implements GameService {

    public GameServiceImpl() {

    }

    @Autowired
    GameDao gameDao;


    public Collection<Game> searchGame(String name, Map<FilterCategory, List<String>> filters) {
        return gameDao.searchGame(name, filters);
    }




}
