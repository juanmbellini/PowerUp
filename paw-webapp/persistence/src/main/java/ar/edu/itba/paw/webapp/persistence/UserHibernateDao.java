package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by julrodriguez on 28/10/16.
 */
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;
    private final GenreDao genreDao;

    @Autowired
    public UserHibernateDao(GameDao gameDao, GenreDao genreDao) {
        this.gameDao = gameDao;
        this.genreDao = genreDao;
    }

    @Override
    public User create(String email, String hashedPassword, String username) throws UserExistsException {
        final User user = new User(email,username, hashedPassword, Authority.USER);
        em.persist(user);
        return user;
    }

    @Override
    public User findById(long id) {
        return DaoHelper.findSingle(em, User.class, id);
    }

    @Override
    public boolean existsWithId(long id) {
        return findById(id) != null;
    }

    @Override
    public User findByUsername(String username) {
        return DaoHelper.findSingleWithConditions(em, User.class, "FROM User as U WHERE U.username = ?1", username);
    }

    @Override
    public boolean existsWithUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public User findByEmail(String email) {
        return DaoHelper.findSingleWithConditions(em, User.class, "FROM User as U WHERE U.email = ?1", email);
    }

    @Override
    public boolean existsWithEmail(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public void scoreGame(long userId, long gameId, int score) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        if(!gameDao.existsWithId(gameId)) {
            throw new NoSuchGameException(gameId);
        }
        user.scoreGame(gameId, score);
        gameDao.updateAvgScore(gameId);
        em.persist(user);
    }

    @Override
    public void setPlayStatus(long userId, long gameId, PlayStatus status) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        if(!gameDao.existsWithId(gameId)) {
            throw new NoSuchGameException(gameId);
        }
        user.setPlayStatus(gameId, status);
        em.persist(user);
    }

    @Override
    public void removeScore(long userId, long gameId) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        user.getScoredGames().remove(gameId);
        em.persist(user);
    }

    @Override
    public void removeStatus(long userId, long gameId) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        user.getPlayStatuses().remove(gameId);
        em.persist(user);
    }

    @Override
    public Collection<Game> recommendGames(long userId) {
        Map<FilterCategory, Map<String, Double>> filtersScoresMap = new HashMap();

        User user = findById(userId);
        Map<Long, Integer> scoredGames = user.getScoredGames();

        if (scoredGames == null || scoredGames.size() == 0) return new LinkedHashSet<>();

        //Genres
        Map<Genre, Integer> countGenre = new HashMap<>();
        Map<Genre, Long> sumGenre = new HashMap<>();
        for (long gameId : scoredGames.keySet()) {
           Game scoredGame = gameDao.findById(gameId);
           for(Genre genre: scoredGame.getGenres()) {
               if(!countGenre.containsKey(genre)) {
                   countGenre.put(genre,0);
               }
               if(!sumGenre.containsKey(genre)){
                   sumGenre.put(genre,0l);
               }
               countGenre.put(genre,countGenre.get(genre)+1);
               sumGenre.put(genre,sumGenre.get(genre)+scoredGames.get(gameId));
           }
        }
        Map<String, Double> mapFilterToFilterScoreGenre = new HashMap<>();
        for(Genre genre: countGenre.keySet()){
            mapFilterToFilterScoreGenre.put(genre.getName(),((double)sumGenre.get(genre))/countGenre.get(genre));
        }
        filtersScoresMap.put(FilterCategory.genre, mapFilterToFilterScoreGenre);

        //Keywords
        Map<Keyword, Integer> countKeyword = new HashMap<>();
        Map<Keyword, Long> sumKeyword = new HashMap<>();
        for (long gameId : scoredGames.keySet()) {
            Game scoredGame = gameDao.findById(gameId);
            for(Keyword keyword: scoredGame.getKeywords()) {
                if(!countKeyword.containsKey(keyword)) {
                    countKeyword.put(keyword,0);
                }
                if(!sumKeyword.containsKey(keyword)){
                    sumKeyword.put(keyword,0l);
                }
                countKeyword.put(keyword,countKeyword.get(keyword)+1);
                sumKeyword.put(keyword,sumKeyword.get(keyword)+scoredGames.get(gameId));
            }
        }
        Map<String, Double> mapFilterToFilterScoreKeyword = new HashMap<>();
        for(Keyword keyword: countKeyword.keySet()){
            mapFilterToFilterScoreKeyword.put(keyword.getName(),((double)sumKeyword.get(keyword))/countKeyword.get(keyword));
        }
        filtersScoresMap.put(FilterCategory.keyword, mapFilterToFilterScoreKeyword);

        //Get all games with each of X filter with higher weight and give each a weight based on avg_score we gave.

        Collection<Game> finalResultList = gameDao.getRecommendedGames(scoredGames.keySet(), filtersScoresMap);

        return finalResultList;

    }

    @Override
    public void setProfilePicture(long userId, byte[] picture) {
        User user = DaoHelper.findSingleOrThrow(em, User.class, userId);
        user.setProfilePicture(picture);
        em.persist(user);
    }

    @Override
    public void removeProfilePicture(long userId) {
        setProfilePicture(userId, null);
    }

//    @Override
//    public Map<Game, PlayStatus> getGameList(long userId, Set<String> playStatusesFilter, Set<String> shelvesFilter) {
//
//        TypedQuery<Game> query = em.createQuery("Select g FROM Game as g, User as u where  ", Game.class);
//
//    }
}
