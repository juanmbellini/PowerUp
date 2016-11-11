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
        for (FilterCategory filterCategory : filters.keySet()) {
            firstArgument = true;
            for (String filter : filters.get(filterCategory)) {

                if (firstArgument) {
                    firstArgument = false;
                    if (!(filterCategory.name().equals("platform"))) {
                        fromString.append(" join g.").append(filterCategory.pretty().toLowerCase()).append("s as ").append(filterCategory.name());
                    } else {
                        fromString.append(" , Platform as platform");
                    }
                    whereString.append(" AND ( ");
                } else {
                    whereString.append(" OR ");
                }
                if (!(filterCategory.name().equals("platform"))) {
                    whereString.append(filterCategory.name()).append(".name = :").append("filter").append(filterToken);
                } else {
                    whereString.append(" ( platform in indices(g.platforms) and platform.name = :" + "filter").append(filterToken).append(" ) ");
                }
                filterToken++;
            }
            if (!firstArgument) whereString.append(" )");
        }
        fromString.append(whereString);

        Query queryCount = em.createQuery(countString + fromString.toString());

        fromString.append(" order by g.").append(Game.getOrderField(orderCategory)).append(ascending ? " ASC" : " DESC");
        TypedQuery<Game> querySelect = em.createQuery(selectString + fromString.toString(), Game.class);


        querySelect.setParameter("name", "%" + name.toLowerCase() + "%");
        queryCount.setParameter("name", "%" + name.toLowerCase() + "%");
        filterToken = 0;
        for (FilterCategory filterCategory : filters.keySet()) {
            for (String filter : filters.get(filterCategory)) {
                querySelect.setParameter("filter" + filterToken, filter);
                queryCount.setParameter("filter" + filterToken, filter);
                filterToken++;
            }
        }

        int count = ((Long) queryCount.getSingleResult()).intValue(); //TODO wat if more?
        int actualPageSize = pageSize == 0 ? count : pageSize;

        querySelect.setFirstResult(actualPageSize * (pageNumber - 1));
        querySelect.setMaxResults(actualPageSize);
        Page<Game> pageResult = new Page<>();
        pageResult.setTotalPages(Math.max((int) Math.ceil((double) count / actualPageSize), 1));
        pageResult.setPageNumber(pageNumber);
        pageResult.setPageSize(actualPageSize);
        pageResult.setOverAllAmountOfElements(count);

        List<Game> list = querySelect.getResultList();
        pageResult.setData(list);
        return pageResult;

    }


    @Override
    public Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending) throws IllegalArgumentException {
        Page<Game> page = searchGames(name, filters, orderCategory, ascending, 0, 1);
        return page.getData();
    }

    @Override
    public Collection<Game> findRelatedGames(long id, Set<FilterCategory> unusedFilters) {

        Game game = findById(id);
        Set<Long> notToIncludeGames = new HashSet<>();
        notToIncludeGames.add(id);
        Map<FilterCategory, Map<String, Double>> filtersScoresMap = new HashMap<>();
        final int genreLimit = 5, keywordLimit = 5, developerLimit = 1;
        final double genreWeight = 10, keywordWeight = 10, developerWeight = 5;
        int countFilter = 0;

        //Consider up to genreLimit genres, with a weight of genreWeight
        Map<String, Double> mapFilterToFilterScoreGenre = new HashMap<>();
        Iterator<Genre> genreIterator = game.getGenres().iterator();
        while (countFilter < genreLimit && genreIterator.hasNext()) {
            mapFilterToFilterScoreGenre.put(genreIterator.next().getName(), genreWeight);
            countFilter++;
        }
        filtersScoresMap.put(FilterCategory.genre, mapFilterToFilterScoreGenre);

        //Analogous, with keywords
        countFilter=0;
        Map<String, Double> mapFilterToFilterScoreKeyword = new HashMap<>();
        Iterator<Keyword> keywordIterator = game.getKeywords().iterator();
        while (countFilter < keywordLimit && keywordIterator.hasNext()) {
            mapFilterToFilterScoreKeyword.put(keywordIterator.next().getName(), keywordWeight);
            countFilter++;
        }
        filtersScoresMap.put(FilterCategory.keyword, mapFilterToFilterScoreKeyword);

        //Analogous, with developers
        countFilter=0;
        Map<String, Double> mapFilterToFilterScoreDeveloper = new HashMap<>();
        Iterator<Developer> developerIterator = game.getDevelopers().iterator();
        while (countFilter < developerLimit && developerIterator.hasNext()) {
            mapFilterToFilterScoreDeveloper.put(developerIterator.next().getName(), developerWeight);
            countFilter++;
        }
        filtersScoresMap.put(FilterCategory.developer, mapFilterToFilterScoreDeveloper);

        return getRecommendedGames(notToIncludeGames, filtersScoresMap);
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
        for (Game game : query.getResultList()) {
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

    public Collection<Game> getRecommendedGames(Set<Long> excludedGameIds, Map<FilterCategory, Map<String, Double>> filtersScoresMap) {
        HashMap<Game, Integer> gamesWeightMap = new HashMap<>();

        //TODO document this method, very hard to understand what each part is doing at a glance

        for (FilterCategory filterCategory : filtersScoresMap.keySet()) {
            Map<String, Double> mapFilter = filtersScoresMap.get(filterCategory);
            for (String filter : mapFilter.keySet()) {
                double filterScore = mapFilter.get(filter);
                HashMap<FilterCategory, List<String>> filterParameterMap = new HashMap<>();
                ArrayList<String> filterArrayParameter = new ArrayList<>();
                filterArrayParameter.add(filter);
                filterParameterMap.put(filterCategory, filterArrayParameter);
                Collection<Game> resultGames  = searchGames("", filterParameterMap, OrderCategory.avg_score, false);
                for (Game game : resultGames) {
                    if (!excludedGameIds.contains(game.getId())) {
                        if(!gamesWeightMap.containsKey(game)){
                            gamesWeightMap.put(game, 0);
                        }
                        gamesWeightMap.put(game, gamesWeightMap.get(game) + (int) filterScore);
                    }
                }
            }
        }

        //Show all games ordered by weight.
        Map<Integer, ArrayList<Game>> gameWeightMapInOrder = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        for (Game game : gamesWeightMap.keySet()) {
            int gameWeight = gamesWeightMap.get(game);
            if (!gameWeightMapInOrder.containsKey(gameWeight)) {
                gameWeightMapInOrder.put(gameWeight, new ArrayList<>());
            }
            gameWeightMapInOrder.get(gameWeight).add(game);

        }

        Collection<Game> finalResultList = new LinkedHashSet<>();
        int counter = 0;
        if (!gameWeightMapInOrder.isEmpty()) {
            Iterator<Integer> weightIterator = gameWeightMapInOrder.keySet().iterator();
            while (weightIterator.hasNext() && counter < 100) {
                int gameWeight = weightIterator.next();
                Iterator<Game> gameIterator = gameWeightMapInOrder.get(gameWeight).iterator();
                while (gameIterator.hasNext() && counter < 100) {
                    finalResultList.add(gameIterator.next());
                    counter++;
                }
            }
        }

        return finalResultList;
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
