package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Julian on 10/30/2016.
 */
@Repository
public class GameHibernateDao implements GameDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException {
        StringBuilder fromString = new StringBuilder("from Game as g ");
        StringBuilder whereString = new StringBuilder(" where g.name like %:name%");
        boolean firstArgument = true;
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                fromString.append(", " + filterCategory.name() + " as " + filterCategory.name()+filter);
                if(!firstArgument){
                    whereString.append("&& " + filterCategory.name()+filter.toString() + ".name = :" + filterCategory.name() + filter);
                }
                firstArgument = false;
            }
        }
        final TypedQuery<User> query = em.createQuery(fromString.toString(), Game.class);
        query.setParameter("name",name);
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                query.setParameter(filterCategory.name() + filter.toString(),filter);
            }
        }
        fromString.append(" order by g.").append(orderCategory.name()).append(ascending ? " ASC" : " DESC");
        List<Game> list = query.getResultList();
        Page<Game> pageResult = new Page<>();
        pageResult.setData(list);
        fromString.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(pageSize * (pageNumber - 1));
        query = em.createQuery(fromString.toString(), Game.class);
        list = query.getResultList();
        pageResult.setPageNumber(pageNumber);
        pageResult.setPageSize(pageSize);
        pageResult.setOverAllAmountOfElements(list.size());
        return pageResult;
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
