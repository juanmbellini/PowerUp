package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Julian on 10/30/2016.
 */
@Repository
public class GameHibernateDao implements GameDao {


    @Override
    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters) {
        return null;
    }

    @Override
    public Game findById(long id) {
        return null;
    }

    @Override
    public boolean existsWithId(long id) {
        return false;
    }

    @Override
    public boolean existsWithTitle(String title) {
        return false;
    }

    @Override
    public Collection<String> getFiltersByType(FilterCategory filterCategory) {
        return null;
    }

    @Override
    public Map<Long, Game> findBasicDataGamesFromArrayId(Collection<Long> ids) {
        return null;
    }

    @Override
    public void updateAvgScore(long gameId) {

    }
}
