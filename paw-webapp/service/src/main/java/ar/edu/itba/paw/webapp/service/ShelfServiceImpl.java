package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;


@Service
@Transactional
public class ShelfServiceImpl implements ShelfService {


    private final ShelfDao shelfDao;

    private final GameDao gameDao;

    private final UserDao userDao;

    @Autowired
    public ShelfServiceImpl(ShelfDao shelfDao, GameDao gameDao, UserDao userDao) {
        this.shelfDao = shelfDao;
        this.gameDao = gameDao;
        this.userDao = userDao;
    }


    @Override
    public Page<Shelf> getShelves(String nameFilter, Long gameIdFilter, String gameNameFilter,
                                  Long userIdFilter, String userNameFilter, int pageNumber, int pageSize,
                                  ShelfDao.SortingType sortingType, SortDirection sortDirection) {
        return shelfDao.getShelves(nameFilter, gameIdFilter, gameNameFilter, userIdFilter, userNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
    }

    @Override
    public Shelf findById(long shelfId) {
        return shelfDao.findById(shelfId);
    }

    @Override
    public Shelf create(String name, long creatorId) {
        return shelfDao.create(name, userDao.findById(creatorId));
    }

    @Override
    public void update(long shelfId, String name, long updaterId) {
        shelfDao.update(checkShelfValuesAndAuthoring(shelfId, updaterId), name);
    }

    @Override
    public void delete(long shelfId, long deleterId) {
        shelfDao.delete(checkShelfValuesAndAuthoring(shelfId, deleterId));
    }

    @Override
    public void addGameToShelf(long shelfId, long gameId, long updaterId) {
        shelfDao.addGameToShelf(checkShelfValuesAndAuthoring(shelfId, updaterId), gameDao.findById(gameId));
    }

    @Override
    public void removeGameFromShelf(long shelfId, long gameId, long updaterId) {
        shelfDao.removeGameFromShelf(checkShelfValuesAndAuthoring(shelfId, updaterId), gameDao.findById(gameId));
    }

    @Override
    public void clearShelf(long shelfId, long updaterId) {
        shelfDao.clearShelf(checkShelfValuesAndAuthoring(shelfId, updaterId));
    }


    /**
     * Checks if the values are correct (including authoring).
     * Upon success,  it returns the {@link Shelf} with the given {@code shelfId}. Otherwise, an exception is thrown.
     *
     * @param shelfId The shelf id.
     * @param userId  The user id.
     * @return The shelf with the given {@code shelfId}.
     * @throws NoSuchEntityException If no {@link Shelf} exists with the given {@code shelfId},
     *                               or if no {@link User} exists with the given {@code userId}.
     * @throws UnauthorizedException If the {@link User} with the given {@code userId} is not the creator
     *                               of the {@link Shelf} with the given {@code shelfId}.
     */
    private Shelf checkShelfValuesAndAuthoring(long shelfId, long userId) throws NoSuchEntityException,
            UnauthorizedException {
        Shelf shelf = checkShelfValues(shelfId, userId);
        if (userId != shelf.getUser().getId()) {
            throw new UnauthorizedException("Shelf #" + shelfId + " does not belong to user #" + userId);
        }
        return shelf;
    }


    /**
     * Checks if the values are correct (without authoring).
     * Upon success, it returns the {@link Shelf} with the given {@code shelfId}. Otherwise, an exception is thrown.
     *
     * @param shelfId The shelf id.
     * @param userId  The user id.
     * @return The shelf with the given {@code shelfId}
     * @throws NoSuchEntityException If no {@link Shelf} exists with the given {@code shelfId},
     *                               or if no {@link User} exists with the given {@code userId}.
     */
    private Shelf checkShelfValues(long shelfId, long userId) throws NoSuchEntityException {
        Shelf shelf = shelfDao.findById(shelfId);
        List<String> missing = new LinkedList<>();
        if (shelf == null) {
            missing.add("shelfId");
        }
        if (userDao.findById(userId) == null) {
            missing.add("userId");
        }
        if (!missing.isEmpty()) {
            throw new NoSuchEntityException(missing);
        }
        return shelf;
    }

}
