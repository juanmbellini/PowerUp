package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class ShelfHibernateDao implements ShelfDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Shelf> getShelves(String nameFilter, Long gameIdFilter, String gameNameFilter,
                                  Long userIdFilter, String userNameFilter,
                                  int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {


        final StringBuilder query = new StringBuilder()
                .append("FROM Shelf shelf LEFT JOIN shelf.games game");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        int conditionNumber = 0;
        if (nameFilter != null && !nameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(shelf.name) LIKE ?" + conditionNumber,
                    "%" + nameFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (gameIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("shelf.game.id = ?" + conditionNumber,
                    gameIdFilter, conditionNumber));
            conditionNumber++;
        }
        if (gameNameFilter != null && !gameNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(shelf.game.name) LIKE ?" + conditionNumber,
                    "%" + gameNameFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (userIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("shelf.user.id = ?" + conditionNumber,
                    userIdFilter, conditionNumber));
            conditionNumber++;
        }
        if (userNameFilter != null && !userNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(shelf.user.username) LIKE ?" + conditionNumber,
                    "%" + userNameFilter.toLowerCase() + "%", conditionNumber));
        }

        return DaoHelper.findPageWithConditions(em, Shelf.class, query, "shelf", "shelf.id", conditions,
                pageNumber, pageSize, "shelf." + sortingType.getFieldName(), sortDirection, true);
    }


    @Override
    public Shelf findById(long shelfId) {
        return em.find(Shelf.class, shelfId);
    }

    @Override
    public Shelf findByName(User owner, String name) {
        return DaoHelper.findByFields(em, Shelf.class, createSearchingMap(owner, name));
    }

    @Override
    public Shelf create(String name, User creator) {
        Shelf shelf = new Shelf(name, creator);
        em.persist(shelf);
        return shelf;
    }

    @Override
    public void update(Shelf shelf, String name) {
        if (shelf == null) {
            throw new IllegalArgumentException();
        }
        shelf.update(name);
        em.merge(shelf);
    }

    @Override
    public void delete(Shelf shelf) {
        if (shelf == null) {
            throw new IllegalArgumentException();
        }
        em.remove(shelf);
    }

    @Override
    public Page<Game> getShelfGames(Shelf shelf, int pageNumber, int pageSize, OrderCategory orderCategory,
                                    SortDirection sortDirection) {

        return DaoHelper.findPageWithConditions(em, Game.class, "FROM ShelfGame shelfGame", "shelfGame.game", "game.id",
                Collections.singletonList(new DaoHelper.ConditionAndParameterWrapper("shelfGame.shelf = ?0", shelf, 0)),
                pageNumber, pageSize, "shelfGame.game." + Game.getOrderField(orderCategory), sortDirection, false);

    }

    @Override
    public void addGameToShelf(Shelf shelf, Game game) {
        if (shelf == null) {
            throw new IllegalArgumentException();
        }
        shelf.addGame(game == null ? null : em.merge(game));
        em.merge(shelf);
    }

    @Override
    public void removeGameFromShelf(Shelf shelf, Game game) {
        if (shelf == null) {
            throw new IllegalArgumentException();
        }
        ShelfGame shelfGame = shelf.removeGame(game);
        if (shelfGame == null) {
            return;
        }
        em.remove(em.merge(shelfGame));
        em.merge(shelf);
    }

    @Override
    public void clearShelf(Shelf shelf) {
        if (shelf == null) {
            throw new IllegalArgumentException();
        }
        shelf.getGames().forEach(game -> em.remove(em.merge(shelf.removeGame(game))));
        em.merge(shelf);
    }


    // ==============
    // Helpers
    // ==============

    /**
     * Creates a map to be used for searching a Shelf in the {@link ShelfHibernateDao#findByName(User, String)} method.
     *
     * @param user The user
     * @param name The shelf name.
     * @return The created map.
     */
    private static Map<String, Object> createSearchingMap(User user, String name) {
        if (user == null || name == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("name", name);
        return map;
    }

}
