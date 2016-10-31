package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.exceptions.notImplementedException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
                    whereString.append("AND " + filterCategory.name()+filter.toString() + ".name = :" + filterCategory.name() + filter);
                }
                firstArgument = false;
            }
        }
        TypedQuery<Game> query = em.createQuery(fromString.toString(), Game.class);
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
    //TODO Diego hacelo vos
    public Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters) {
        throw new notImplementedException();
    }

    @Override
    public Game findById(long id) {
        TypedQuery<Game> baseQuery = em.createQuery("FROM Game AS G where G.id = :id", Game.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Game2 findById2(long id) {
        TypedQuery<Game2> baseQuery = em.createQuery("FROM Game2 AS G where G.id = :id", Game2.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean existsWithId(long id) {
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.id = :id", Game.class);
        query.setParameter("id", id);
        final List<Game> list = query.getResultList();
        return !list.isEmpty();
    }

    @Override
    public boolean existsWithTitle(String title) {
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.name = :title", Game.class);
        query.setParameter("title", title);
        final List<Game> list = query.getResultList();
        return !list.isEmpty();
    }

    @Override
    @Deprecated
    public Collection<String> getFiltersByType(FilterCategory filterCategory) {
        throw new notImplementedException();
    }

    @Override
    public Map<Long, Game> findBasicDataGamesFromArrayId(Collection<Long> ids) {
        return null;
    }

    @Override
    public void updateAvgScore(long gameId) {
        throw new notImplementedException();
    }
}
