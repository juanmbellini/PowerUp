package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameDao gameDao;

    public GameServiceImpl() {
    }

    @Override
    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters,
                                  OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber) {
        name = escapeUnsafeCharacters(name);
        return gameDao.searchGames(name, filters, orderCategory, ascending, pageSize, pageNumber);
    }

    @Override
    public Collection<Game> findRelatedGames(long gameId, Set<FilterCategory> filters) {
        return gameDao.findRelatedGames(gameId, filters);
    }

    @Override
    public Game findById(long id) {
        return gameDao.findById(id);
    }

    @Override
    public boolean existsWithId(long id) {
        return gameDao.existsWithId(id);
    }

    @Override
    public boolean existsWithTitle(String title) {
        return gameDao.existsWithTitle(title);
    }

    public Collection<String> getFiltersByType(FilterCategory filterCategory) {
        return gameDao.getFiltersByType(filterCategory);
    }

    @Override
    public Collection<Genre> getGenres(long gameId) {
        return gameDao.getGenres(gameId);
    }

    @Override
    public Map<Platform, GamePlatformReleaseDate> getPlatforms(long gameId) {
        return gameDao.getPlatforms(gameId);
    }

    @Override
    public Collection<Publisher> getPublishers(long gameId) {
        return gameDao.getPublishers(gameId);
    }

    @Override
    public Collection<Developer> getDevelopers(long gameId) {
        return gameDao.getDevelopers(gameId);
    }

    @Override
    public Collection<Keyword> getKeywords(long gameId) {
        return gameDao.getKeywords(gameId);
    }

    @Override
    public Collection<Review> getReviews(long gameId) {
        return gameDao.getReviews(gameId);
    }

    @Override
    public Map<Long, Game> findByIds(Collection<Long> ids) {
        return gameDao.findByIds(ids);
    }

    // TODO: Move to controller as this is a controller's task
    public String escapeUnsafeCharacters(String name){
        char[] escape = new char[1];
        StringBuilder nameEscaped = new StringBuilder();
        for(int i = 0; i < name.length(); i++){
            if(name.charAt(i) == '%' || name.charAt(i) == '_' || name.charAt(i) == '\\'){
                nameEscaped.append('\\');
            }
            nameEscaped.append(name.charAt(i));
        }
        return nameEscaped.toString();
    }
}
