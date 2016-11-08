package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.notImplementedException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Julian on 10/30/2016.
 */
@Repository
public class GameHibernateDao implements GameDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException {

        String selectString = "select g ";
        String countString = "select count(distinct g.id) ";
        StringBuilder fromString = new StringBuilder("from Game as g ");
        StringBuilder whereString = new StringBuilder(" where LOWER(g.name) like :name");
        boolean firstArgument = true;
        long filterToken = 0;
        for(FilterCategory filterCategory: filters.keySet()){
            firstArgument=true;
            for(String filter: filters.get(filterCategory)){

                if(firstArgument){
                    firstArgument=false;
                    fromString.append(" join g." + filterCategory.pretty().toLowerCase() + "s as " + filterCategory.name());
                    whereString.append(" AND ( ");
                }else{
                    whereString.append(" OR ");
                }

                if(!(filterCategory.name().equals("platform"))){
                    whereString.append(filterCategory.name() + ".name = :" + "filter"+filterToken);
                }else{
                    whereString.append("index("+filterCategory.name()+").name = :" + "filter"+filterToken);
                }
                filterToken++;
            }
            if(!firstArgument) whereString.append(" )");
        }
        fromString.append(whereString);

        Query queryCount = em.createQuery(countString+fromString.toString());

        fromString.append(" order by g.").append(orderCategory.name()).append(ascending ? " ASC" : " DESC");
        TypedQuery<Game> querySelect = em.createQuery(selectString+fromString.toString(), Game.class);
        querySelect.setFirstResult(pageSize * (pageNumber - 1));
        querySelect.setMaxResults(pageSize);

        querySelect.setParameter("name", "%"+name.toLowerCase()+"%");
        queryCount.setParameter("name", "%"+name.toLowerCase()+"%");
        filterToken=0;
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                if(!(filterCategory.name().equals("platform"))) {
                    querySelect.setParameter("filter"+filterToken, filter);
                    queryCount.setParameter("filter"+filterToken, filter);
                    filterToken++;
                }
            }
        }

        Long count = (Long)queryCount.getSingleResult();

        Page<Game> pageResult = new Page<>();
        pageResult.setTotalPages(Math.max((int)Math.ceil((double)count / pageSize), 1));
        pageResult.setPageNumber(pageNumber);
        pageResult.setPageSize(pageSize);
        pageResult.setOverAllAmountOfElements(count);

        List<Game> list = querySelect.getResultList();
        pageResult.setData(list);
        return pageResult;


//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
////
////        //Build queries for each filter
////        for(FilterCategory category : filters.keySet()) {
////            switch (category) {
////                case developer:
////                    criteriaBuilder.createQuery(Developer.class);
////
////
//////                    Join<Game, Developer> developerJoin = gamesRoot.join(new CollectionJoin<>());
//////                    gamesRoot.join()
//////                    conditions2.add(criteriaBuilder.in())
//////                    break;
////            }
////            criteriaQuery.where(criteriaBuilder.in(gamesRoot.get("platforms"), "%"+name+"%"));
////        }
////
////
////
////
////
//        CriteriaQuery<Game> criteriaQuery = criteriaBuilder.createQuery(Game.class);
//        Root<Game> gamesRoot = criteriaQuery.from(Game.class);
//        criteriaQuery.select(gamesRoot);
//
//        //Create and add all conditions
//        List<Predicate> conditions2 = new ArrayList<>();
//        if (name != null) {
//            conditions2.add(criteriaBuilder.like(gamesRoot.get("name"), "%"+name+"%"));
//        }
//
//        criteriaQuery.where(conditions2.toArray(new Predicate[0]));
//
//        List<Game> games = em.createQuery(criteriaQuery).setFirstResult(pageSize * (pageNumber - 1)).setMaxResults(pageSize).getResultList();
//        Page<Game> page = new Page<>();
//        page.setTotalPages(Math.max((int)Math.floor(games.size() / pageSize), 1));
//        page.setPageNumber(pageNumber);
//        page.setPageSize(pageSize);
//        page.setOverAllAmountOfElements(games.size());
//        page.setData(games);
//
//        return page;
    }

    public Page<Game> searchGames2(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException {
        StringBuilder fromString = new StringBuilder("from Game as g ");
        StringBuilder whereString = new StringBuilder(" where LOWER(g.name) like :name");
        boolean firstArgument = true;
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                if(!(filterCategory.name() == "platform")){
                    fromString.append(" join g." + filterCategory.pretty().toLowerCase() + "s as " + filter +filterCategory.name());
                    whereString.append(" AND " + filterCategory.name()+filter.toString() + ".name = :" + filterCategory.name() + filter);
                }
                else{

                }
            }
        }
        fromString.append(whereString);
        TypedQuery<Game> query = em.createQuery(fromString.toString(), Game.class);
        query.setParameter("name", "%"+name.toLowerCase()+"%");
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                if(!(filterCategory.name() == "platform")) {
                    query.setParameter(filterCategory.name() + filter.toString(), filter);
                }
            }
        }
        fromString.append(" order by g.").append(orderCategory.name()).append(ascending ? " ASC" : " DESC");
        List<Game> list = query.getResultList();
        Page<Game> pageResult = new Page<>();
        pageResult.setTotalPages(Math.max((int)Math.floor(list.size() / pageSize), 1));
        pageResult.setPageNumber(pageNumber);
        pageResult.setPageSize(pageSize);
        pageResult.setData(list);
//        fromString.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(pageSize * (pageNumber - 1));
        query = em.createQuery(fromString.toString(), Game.class);
        query.setFirstResult(pageSize * (pageNumber - 1));
        query.setMaxResults(pageSize);
        //We have to re-bind parameters after changing the query
        query.setParameter("name", "%"+name.toLowerCase()+"%");
        for(FilterCategory filterCategory: filters.keySet()){
            for(String filter: filters.get(filterCategory)){
                if(!(filterCategory.name() == "platform")) {
                    query.setParameter(filterCategory.name() + filter.toString(), filter);
                }
            }
        }
        list = query.getResultList();
        pageResult.setOverAllAmountOfElements(list.size());
        return pageResult;
    }

    @Override
    public Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending) throws IllegalArgumentException {
        return null;
    }

    @Override
    //TODO Diego hacelo vos
    public Set<Game> findRelatedGames(long baseGameId, Set<FilterCategory> filters) {
        throw new notImplementedException();
    }

    @Override
    public Game findById(long id) {
        return em.find(Game.class, id);
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
    public Map<Long, Game> findByIds(Collection<Long> ids) {
        final Map<Long, Game> result = new HashMap<>();
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.id IN (:ids)", Game.class);
        query.setParameter("ids", ids);
        for(Game game : query.getResultList()) {
            result.put(game.getId(), game);
        }
        return result;
    }

    @Override
    public void updateAvgScore(long gameId) {
        throw new notImplementedException();
    }

    @Override
    public Collection<Genre> getGenres(long gameId) {
        return getFreshGame(gameId).getGenres();
    }

    @Override
    public Map<Platform, GamePlatformReleaseDate> getPlatforms(long gameId) {
        return getFreshGame(gameId).getPlatforms();
    }

    @Override
    public Collection<Publisher> getPublishers(long gameId) {
        return getFreshGame(gameId).getPublishers();
    }

    @Override
    public Collection<Developer> getDevelopers(long gameId) {
        return getFreshGame(gameId).getDevelopers();
    }

    @Override
    public Collection<Keyword> getKeywords(long gameId) {
        return getFreshGame(gameId).getKeywords();
    }

    @Override
    public Collection<Review> getReviews(long gameId) {
        return Collections.emptySet();
        //TODO implement
//        return getFreshGame(gameId).getReviews();
    }

    /**
     * Gets a game by the specified ID that is transaction-safe (i.e. lazily-initialized collections can be accessed)
     * and throws exception if not found. If present in current transaction context, the game is returned from there
     * instead of querying the database.
     *
     * @param gameId The ID of the game to fetch.
     * @return The found game.
     * @throws NoSuchGameException If no such game exists.
     */
    private Game getFreshGame(long gameId) {
        Game result = em.find(Game.class, gameId);
        if (result == null) {
            throw new NoSuchGameException(gameId);
        }
        return result;
    }
}
