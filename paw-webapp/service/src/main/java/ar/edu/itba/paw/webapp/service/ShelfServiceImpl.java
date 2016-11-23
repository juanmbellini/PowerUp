package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.model.Shelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfDao shelfDao;

    @Override
    public Shelf create(String name, long creatorUserId, long... initialGameIds) throws NoSuchEntityException {
        return shelfDao.create(name, creatorUserId, initialGameIds);
    }

    @Override
    public Set<Shelf> findByGameId(long id) {
        return shelfDao.findByGameId(id);
    }

    @Override
    public Set<Shelf> findByGameName(String name) {
        return shelfDao.findByGameName(name);
    }

    @Override
    public Set<Shelf> findByUserId(long id) {
        return shelfDao.findByUserId(id);
    }

    @Override
    public Set<Shelf> findByUsername(String name) {
        return shelfDao.findByUsername(name);
    }

    @Override
    public Shelf findById(long shelfId) {
        return shelfDao.findById(shelfId);
    }

    @Override
    public Set<Shelf> findByName(String shelfName) {
        return shelfDao.findByName(shelfName);
    }

    @Override
    public boolean belongsTo(long shelfId, long userId) throws NoSuchEntityException {
        return shelfDao.belongsTo(shelfId, userId);
    }

    @Override
    public void rename(long shelfId, String newName) throws NoSuchEntityException, IllegalArgumentException {
        shelfDao.rename(shelfId, newName);
    }

    @Override
    public void update(long shelfId, long... newGameIds) throws NoSuchEntityException {
        shelfDao.update(shelfId, newGameIds);
    }

    @Override
    public void addGame(long shelfId, long gameId) throws NoSuchEntityException {
        shelfDao.addGame(shelfId, gameId);
    }

    @Override
    public void removeGame(long shelfId, long gameId) throws NoSuchEntityException {
        shelfDao.removeGame(shelfId, gameId);
    }

    @Override
    public void clear(long shelfId) throws NoSuchEntityException {
        shelfDao.clear(shelfId);
    }

    @Override
    public void delete(long shelfId) throws NoSuchEntityException {
        shelfDao.delete(shelfId);
    }
}
