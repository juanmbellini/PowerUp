package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.ShelfGame;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ShelfHibernateDao implements ShelfDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Shelf> getShelves(String nameFilter, Long gameIdFilter, String gameNameFilter,
                                  Long userIdFilter, String userNameFilter,
                                  int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {


        if ((nameFilter != null && nameFilter.isEmpty()) || (userIdFilter != null && userIdFilter <= 0)
                || (userNameFilter != null && userNameFilter.isEmpty())
                || (gameIdFilter != null && gameIdFilter <= 0)
                || (gameNameFilter != null && gameNameFilter.isEmpty())) {
            throw new IllegalArgumentException();
        }

        final StringBuilder query = new StringBuilder().append("FROM Shelf shelf LEFT JOIN games game");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        if (nameFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("name = :shelfName",
                    "%" + nameFilter + "%"));
        }
        if (gameIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("game.id = :gameId", gameIdFilter));
        }
        if (gameNameFilter != null && !gameNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("game.name = :gameName",
                    "%" + gameNameFilter + "%"));
        }
        if (userIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.id = :userId", userIdFilter));
        }
        if (userNameFilter != null && !userNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.username = :userName",
                    "%" + userNameFilter + "%"));
        }
        DaoHelper.appendConditions(query, conditions);

        // Sorting
        query.append(" ORDER BY ").append(sortingType.getFieldName()).append(" ").append(sortDirection.getQLKeyword());

        return DaoHelper.findPageWithConditions(em, Shelf.class, pageNumber, pageSize, query.toString(),
                conditions.stream().map(DaoHelper.ConditionAndParameterWrapper::getParameter).toArray());
    }


    @Override
    public Shelf findById(long shelfId) {
        return em.find(Shelf.class, shelfId);
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


}
