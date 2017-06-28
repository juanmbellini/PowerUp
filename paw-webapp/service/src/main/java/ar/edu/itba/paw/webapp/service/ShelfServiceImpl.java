package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthenticatedException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.function.BiPredicate;


@Service
@Transactional
public class ShelfServiceImpl implements ShelfService, ValidationExceptionThrower {


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
    public Page<Shelf> getUserShelves(long ownerId, String nameFilter, Long gameIdFilter, String gameNameFilter,
                                      int pageNumber, int pageSize, ShelfDao.SortingType sortingType,
                                      SortDirection sortDirection) {
        getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        return shelfDao.getShelves(nameFilter, gameIdFilter, gameNameFilter, ownerId, null,
                pageNumber, pageSize, sortingType, sortDirection);
    }

    @Override
    public Shelf findById(long shelfId) {
        return shelfDao.findById(shelfId);
    }

    @Override
    public Shelf findByName(long userId, String name) {
        User user = userDao.findById(userId);
        return user == null ? null : shelfDao.findByName(user, name);
    }

    @Override
    public Shelf create(long ownerId, String name, User creator) {
        if (creator == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateCreationPermission(owner, creator);
        validateNameAvailability(owner, name);
        return shelfDao.create(name, owner);
    }

    @Override
    public void update(long ownerId, String name, String newName, User updater) {
        if (updater == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateUpdatePermission(owner, updater);
        final Shelf shelf = getShelf(owner, name); // Throws NoSuchEntityException if it not exists.
        if (name.equals(newName)) {
            return; // Avoid querying database for name availability and updating.
        }
        validateNameAvailability(owner, newName);
        shelfDao.update(shelf, newName);

    }


    @Override
    public void delete(long ownerId, String name, User deleter) {
        if (deleter == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateDeletePermission(owner, deleter);
        getShelfOptional(owner, name).ifPresent(shelfDao::delete); // If not present, do nothing (and be idempotent).
    }

    @Override
    public Page<Game> getShelfGames(long ownerId, String shelfName, int pageNumber, int pageSize,
                                    OrderCategory orderCategory, SortDirection sortDirection) {
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        final Shelf shelf = getShelf(owner, shelfName); // Throws NoSuchEntityException if it not exists.
        return shelfDao.getShelfGames(shelf, pageNumber, pageSize, orderCategory, sortDirection);
    }

    @Override
    public void addGameToShelf(long ownerId, String shelfName, long gameId, User updater) {
        if (updater == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateUpdatePermission(owner, updater);
        final Shelf shelf = getShelf(owner, shelfName); // Throws NoSuchEntityException if it not exists.
        shelfDao.addGameToShelf(shelf, gameDao.findById(gameId)); // If game does not exists, lower layer will handle.
    }

    @Override
    public void removeGameFromShelf(long ownerId, String shelfName, long gameId, User updater) {
        if (updater == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateUpdatePermission(owner, updater);
        final Shelf shelf = getShelf(owner, shelfName); // Throws NoSuchEntityException if it not exists.
        shelfDao.removeGameFromShelf(shelf, gameDao.findById(gameId)); // If game does not exists, lower layer will handle.
    }

    @Override
    public void clearShelf(long ownerId, String shelfName, User updater) {
        if (updater == null) {
            throw new UnauthenticatedException();
        }
        final User owner = getOwner(ownerId); // Throws NoSuchEntityException if it not exists.
        validateUpdatePermission(owner, updater);
        final Shelf shelf = getShelf(owner, shelfName); // Throws NoSuchEntityException if it not exists.
        shelfDao.clearShelf(shelf);
    }


    /**
     * Retrieves the {@link User} with the given {@code ownerId}.
     *
     * @param ownerId The user id.
     * @return The {@link User} whose id matches the given {@code ownerId}.
     * @throws NoSuchEntityException If no {@link User} exists with the given {@code ownerId}.
     */
    private User getOwner(final long ownerId) throws NoSuchEntityException {
        return Optional.ofNullable(userDao.findById(ownerId))
                .orElseThrow(NoSuchEntityException::new);
    }


    /**
     * Checks that the given {@code creator} has permission to create a new {@link Shelf}
     * for the given {@code owner}.
     *
     * @param owner   The {@link User} that will be the future owner of the new {@link Shelf}.
     * @param creator The {@link User} performing the operation.
     */
    private void validateCreationPermission(final User owner, final User creator) {
        validatePermission(owner, creator, "create",
                (ownerUser, creatorUser) -> Long.compare(ownerUser.getId(), creatorUser.getId()) == 0);
    }

    /**
     * Checks that the given {@code updater} has permission to update a {@link Shelf} owned by the given {@code owner}.
     *
     * @param owner   The {@link User} owning the {@link Shelf}
     * @param updater The {@link User} performing the operation.
     */
    private void validateUpdatePermission(final User owner, final User updater) {
        validatePermission(owner, updater, "update",
                (ownerUser, updaterUser) -> Long.compare(ownerUser.getId(), updaterUser.getId()) == 0);
    }

    /**
     * Checks that the given {@code deleter} has permission to delete a {@link Shelf} owned by the given {@code owner}.
     *
     * @param owner   The {@link User} owning the {@link Shelf}
     * @param deleter The {@link User} performing the operation.
     */
    private void validateDeletePermission(final User owner, final User deleter) {
        validatePermission(owner, deleter, "delete",
                (ownerUser, deleterUser) -> Long.compare(ownerUser.getId(), deleterUser.getId()) == 0);
    }

    /**
     * Validates that the given {@code operator} can perform the given {@code operationName}
     * on a {@link Shelf} whose owner is the given {@code owner}, according to the given
     * {@link BiPredicate}. (which must implement the {@link BiPredicate#test(Object, Object)} in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Shelf} of the owner, or {@code false}
     * otherwise).
     *
     * @param owner         The {@link User} who owns the {@link Shelf}.
     * @param operator      The {@link User} who is operating over the {@link Shelf}.
     * @param operationName A {@link String} indicating the operation being done (for informative purposes).
     * @param testFunction  A {@link BiPredicate} that implements logic to check if the operation is allowed.
     * @throws UnauthorizedException If the {@code operator} {@link User} does not have permission to operate over
     *                               the {@link Shelf} owned by the {@code owner} {@link User}.
     * @implNote The {@link BiPredicate#test(Object, Object)} must be implemented in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Shelf} of the owner, or {@code false}
     * otherwise
     */
    private void validatePermission(final User owner, final User operator,
                                    String operationName, BiPredicate<User, User> testFunction)
            throws UnauthorizedException {
        if (owner == null || operator == null) {
            throw new IllegalArgumentException("Owner and Operator must not be null");
        }
        if (testFunction.negate().test(owner, operator)) {
            throw new UnauthorizedException("User #" + operator.getId() + " does not have permission" +
                    " to " + operationName + " a shelf for User #" + owner.getId());
        }
    }


    /**
     * Checks whether a {@link Shelf} exists with the given {@code shelfName},
     * whose owner is the given {@link User}.
     *
     * @param user      The {@link User} to check if it owns a {@link Shelf} with the given {@code shelfName}.
     * @param shelfName The {@link Shelf} name.
     * @throws ValidationException If the given {@link User} owns a {@link Shelf} with the given {@code shelfName}.
     */
    private void validateNameAvailability(final User user, final String shelfName) throws ValidationException {
        if (user == null || shelfName == null) {
            throw new IllegalArgumentException("User and name must not be null");
        }
        // TODO: add existence method
        if (shelfDao.findByName(user, shelfName) != null) {
            throwValidationException(Collections.singletonList(SHELF_NAME_IN_USE));
        }
    }


    /**
     * Retrieves the {@link Shelf} with the given {@code shelfName}, owned by the given {@code owner}.
     *
     * @param owner     The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The {@link Shelf} with the given {@code shelfName}, owned by the given {@code owner}.
     * @throws NoSuchEntityException If the {@link Shelf} does not exists.
     */
    private Shelf getShelf(final User owner, final String shelfName) throws NoSuchEntityException {
        return getShelfOptional(owner, shelfName).orElseThrow(NoSuchEntityException::new);
    }

    /**
     * Retrieves a nullable {@link Optional} of {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code owner}.
     *
     * @param owner     The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The nullable {@link Optional}.
     */
    private Optional<Shelf> getShelfOptional(final User owner, final String shelfName) {
        if (owner == null || shelfName == null) {
            throw new IllegalArgumentException("Owner and shelf name must not be null");
        }
        return Optional.ofNullable(shelfDao.findByName(owner, shelfName));
    }


    private static final ValueError SHELF_NAME_IN_USE =
            new ValueError(ValueError.ErrorCause.ALREADY_EXISTS, "name",
                    "That name is already in use for the given user.");

}
