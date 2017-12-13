package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link GameListService}.
 */
@Service
@Transactional(readOnly = true)
public class GameListServiceImpl implements GameListService {

    private final ShelfDao shelfDao;

    private final UserDao userDao;

    private final GameDao gameDao;

    @Autowired
    public GameListServiceImpl(ShelfDao shelfDao, UserDao userDao, GameDao gameDao) {
        this.shelfDao = shelfDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    @Override
    public Page<GameWithUserShelvesWrapper> getGameList(long userId, List<String> shelfNames, List<PlayStatus> statuses,
                                                        int pageNumber, int pageSize,
                                                        UserDao.ListGameSortingType sortingType,
                                                        SortDirection sortDirection) {
        final Object[] args = {shelfNames, statuses};
        Assert.noNullElements(args, "The shelf names list and the statuses list must not be null");

        final User user = getUser(userId); // Will throw NoSuchEntityException if not exists
        final List<Shelf> shelves = shelfNames.stream().map(name -> getShelf(user, name)).collect(Collectors.toList());

        final Page<Game> page = userDao.getGameList(user, shelves, statuses,
                pageNumber, pageSize, sortingType, sortDirection);

        // Gets all shelves of the given user
        final int amountOfShelves = (int) shelfDao.getShelves(null, null, null, user.getId(), null, 1, 1,
                ShelfDao.SortingType.ID, SortDirection.ASC).getOverAllAmountOfElements();
        final Collection<Shelf> userShelves = shelfDao.getShelves(null, null, null, user.getId(), null, 1, amountOfShelves,
                ShelfDao.SortingType.ID, SortDirection.ASC).getData();

        return ServiceHelper.fromAnotherPage(page, game -> {
            final List<Shelf> shelvesHolding = userShelves.stream().filter(shelf -> shelf.getGames().contains(game))
                    .collect(Collectors.toList());

            final Iterator<Integer> scoresIt = userDao.getGameScores(user, game.getId(), null, 1, 1,
                    UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC)
                    .getData().stream().map(UserGameScore::getScore).iterator();
            final Iterator<PlayStatus> statusesIt = userDao.getPlayStatuses(user, game.getId(), null, 1, 1,
                    UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC)
                    .getData().stream().map(UserGameStatus::getPlayStatus).iterator();

            return new GameWithUserShelvesWrapper(user, game,
                    shelvesHolding,
                    scoresIt.hasNext() ? scoresIt.next() : null,
                    statusesIt.hasNext() ? statusesIt.next() : null);
        }).build();
    }

    @Override
    public boolean belongsToGameList(long userId, long gameId) {
//        final List<PlayStatus> validPlayStatuses = Stream
//                .of(PlayStatus.PLAN_TO_PLAY, PlayStatus.PLAYED, PlayStatus.PLAYING)
//                .collect(Collectors.toList());
//        final long amount = getGameList(userId, new LinkedList<>(), validPlayStatuses,
//                1, 1, UserDao.ListGameSortingType.GAME_NAME, SortDirection.ASC)
//                .getOverAllAmountOfElements();
//
//        return getGameList(userId, new LinkedList<>(), validPlayStatuses,
//                1, (int) amount, UserDao.ListGameSortingType.GAME_NAME, SortDirection.ASC)
//                .getData().stream()
//                .map(GameWithUserShelvesWrapper::getGame)
//                .map(Game::getId)
//                .filter(id -> id == gameId)
//                .count() > 0;
        final User user = getUser(userId);
        boolean hasPlayStatus = false;
        Collection<UserGameStatus> playStatuses = userDao.getPlayStatuses(user, gameId, null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData();
        if (playStatuses != null && playStatuses.iterator().hasNext()) {
            UserGameStatus ugs = playStatuses.iterator().next();
            if (ugs == null) {
                userDao.setPlayStatus(user, Optional.ofNullable(gameDao.findById(gameId)).orElseThrow(NoSuchElementException::new), PlayStatus.NO_PLAY_STATUS);
            } else {
                if (!ugs.getPlayStatus().equals(PlayStatus.NO_PLAY_STATUS)) hasPlayStatus = true;
            }
        }
        return !userDao.getGameScores(user, gameId, null, 1, 1, UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC).getData().isEmpty()
                || !shelfDao.getShelves(null, gameId, null, userId, null, 1, 1, ShelfDao.SortingType.ID, SortDirection.ASC).isEmpty()
                || hasPlayStatus;
    }


    // ======== Helper methods

    /**
     * Retrieves the {@link User} with the given {@code userId}.
     *
     * @param userId The user id.
     * @return The {@link User} whose id matches the given {@code userId}.
     * @throws NoSuchEntityException If no {@link User} exists with the given {@code userId}.
     */
    private User getUser(final long userId) throws NoSuchEntityException {
        return Optional.ofNullable(userDao.findById(userId))
                .orElseThrow(NoSuchEntityException::new);
    }


    /**
     * Retrieves the {@link Shelf} with the given {@code shelfName}, owned by the given {@code user}.
     *
     * @param user      The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The {@link Shelf} with the given {@code shelfName}, owned by the given {@code user}.
     * @throws NoSuchEntityException If the {@link Shelf} does not exists.
     */
    private Shelf getShelf(final User user, final String shelfName) throws NoSuchEntityException {
        return getShelfOptional(user, shelfName).orElseThrow(NoSuchEntityException::new);
    }

    /**
     * Retrieves a nullable {@link Optional} of {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code user}.
     *
     * @param user      The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The nullable {@link Optional}.
     */
    private Optional<Shelf> getShelfOptional(final User user, final String shelfName) {
        final Object[] args = {user, shelfName};
        Assert.noNullElements(args, "User and shelf name must not be null");

        return Optional.ofNullable(shelfDao.findByName(user, shelfName));
    }
}
