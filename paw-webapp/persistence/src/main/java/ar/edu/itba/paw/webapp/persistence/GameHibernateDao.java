package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NumberOfPageBiggerThanTotalAmountException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.hibernate.Hibernate;
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
    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory,
                                  boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException {

        if (name == null || filters == null || orderCategory == null || pageSize < 0 || pageNumber <= 0) {
            throw new IllegalArgumentException();
        }
        name = DaoHelper.escapeUnsafeCharacters(name); // First we sanitize the string values.

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
                        fromString.append(" join g.").append(filterCategory.name().toLowerCase()).append("s as ").append(filterCategory.name());
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

        fromString.append(" order by ")
                .append(orderCategory == OrderCategory.AVG_SCORE ? "NULLIF(" : "")
                .append("g.")
                .append(orderCategory.getFieldName())
                .append(orderCategory == OrderCategory.AVG_SCORE ? ",0)" : "")
                .append(ascending ? " ASC NULLS LAST" : " DESC NULLS LAST");

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
        if (count == 0) {
            return Page.emptyPage();
        }
        int actualPageSize = pageSize == 0 ? count : pageSize;
        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / actualPageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }
        return new Page.Builder<Game>()
                .setTotalPages(totalAmountOfPages)
                .setPageNumber(pageNumber)
                .setPageSize(actualPageSize)
                .setOverAllAmountOfElements(count)
                .setData(querySelect
                        .setFirstResult(actualPageSize * (pageNumber - 1))
                        .setMaxResults(actualPageSize)
                        .getResultList())
                .build();
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
        countFilter = 0;
        Map<String, Double> mapFilterToFilterScoreKeyword = new HashMap<>();
        Iterator<Keyword> keywordIterator = game.getKeywords().iterator();
        while (countFilter < keywordLimit && keywordIterator.hasNext()) {
            mapFilterToFilterScoreKeyword.put(keywordIterator.next().getName(), keywordWeight);
            countFilter++;
        }
        filtersScoresMap.put(FilterCategory.keyword, mapFilterToFilterScoreKeyword);

        //Analogous, with developers
        countFilter = 0;
        Map<String, Double> mapFilterToFilterScoreDeveloper = new HashMap<>();
        Iterator<Company> developerIterator = game.getDevelopers().iterator();
        while (countFilter < developerLimit && developerIterator.hasNext()) {
            mapFilterToFilterScoreDeveloper.put(developerIterator.next().getName(), developerWeight);
            countFilter++;
        }
        filtersScoresMap.put(FilterCategory.developer, mapFilterToFilterScoreDeveloper);

        return getRecommendedGames(notToIncludeGames, filtersScoresMap);
    }

    @Override
    public Game findById(long id) {
        Game game = DaoHelper.findSingle(em, Game.class, id);
        if (game == null) {
            return null;
        }
        // TODO: move to service layer?
        loadGenres(game);
        loadPlatforms(game);
        loadDevelopers(game);
        loadPublishers(game);
        loadKeywords(game);
        loadPictures(game);
        loadVideos(game);
        return game;
    }

    @Override
    public boolean existsWithId(long id) {
        return findById(id) != null;
    }

    @Override
    public boolean existsWithTitle(String title) {
        return DaoHelper.findSingleWithConditions(em, Game.class, "FROM Game AS G WHERE G.name = ?1", title) != null;
    }

    @Override
    public Map<Long, Game> findByIds(Collection<Long> ids) {
        final Map<Long, Game> result = new HashMap<>();
        final List<Game> games = DaoHelper.findManyWithConditions(em, Game.class, "FROM Game AS G WHERE G.id IN (?1)", ids);
        for (Game game : games) {
            result.put(game.getId(), game);
        }
        return result;
    }

    @Override
    public void updateAvgScore(long gameId) {
        Game game = findById(gameId);
        Double newAvg = DaoHelper.findSingleWithConditions(em, Double.class, "SELECT AVG(ELEMENTS(G.scores)) FROM Game AS G WHERE G.id = ?1", gameId);
        if (newAvg == null) newAvg = 0d;
        game.setAvgScore(newAvg);
    }

    @Override
    public Collection<Genre> getGenres(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getGenres();
    }

    @Override
    public Map<Platform, GamePlatformReleaseDate> getPlatforms(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getPlatforms();
    }

    @Override
    public Collection<Company> getPublishers(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getPublishers();
    }

    @Override
    public Collection<Company> getDevelopers(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getDevelopers();
    }

    @Override
    public Collection<Keyword> getKeywords(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getKeywords();
    }

    @Override
    public Collection<Review> getReviews(long gameId) {
        return Collections.emptySet();
        //TODO implement
//        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getReviews();
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
                Collection<Game> resultGames = searchGames("", filterParameterMap, OrderCategory.AVG_SCORE, false);
                for (Game game : resultGames) {
                    if (!excludedGameIds.contains(game.getId())) {
                        if (!gamesWeightMap.containsKey(game)) {
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

    @Override
    public Map<String, String> getVideos(long gameId) {
        return new HashMap<>(DaoHelper.findSingleOrThrow(em, Game.class, gameId).getVideos());
    }

    @Override
    public Set<String> getPictureUrls(long gameId) {
        return new LinkedHashSet<>(DaoHelper.findSingleOrThrow(em, Game.class, gameId).getPictureUrls());
    }

    @Override
    public Map<Long, Integer> getScores(long gameId) {
        return DaoHelper.findSingleOrThrow(em, Game.class, gameId).getScores();
    }

    @Override
    public GameDao loadGenres(Game game) {
        Hibernate.initialize(game.getGenres());
        return this;
    }

    @Override
    public GameDao loadPlatforms(Game game) {
        Hibernate.initialize(game.getPlatforms());
        return this;
    }

    @Override
    public GameDao loadDevelopers(Game game) {
        Hibernate.initialize(game.getDevelopers());
        return this;
    }

    @Override
    public GameDao loadPublishers(Game game) {
        Hibernate.initialize(game.getPublishers());
        return this;
    }

    @Override
    public GameDao loadKeywords(Game game) {
        Hibernate.initialize(game.getKeywords());
        return this;
    }

    @Override
    public GameDao loadPictures(Game game) {
        Hibernate.initialize(game.getPictureIds());
        return this;
    }

    @Override
    public GameDao loadVideos(Game game) {
        Hibernate.initialize(game.getVideos());
        return this;
    }
}
