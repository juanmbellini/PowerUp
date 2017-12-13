package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.List;

/**
 * Defines behaviour for an object that provides services for the game list.
 */
public interface GameListService {

    /**
     * Returns a {@link ar.edu.itba.paw.webapp.model.User}'s list
     * (i.e games in a {@link ar.edu.itba.paw.webapp.model.Shelf} or with {@link PlayStatus}).
     * Filtering and sorting can be applied.
     *
     * @param userId        The {@link ar.edu.itba.paw.webapp.model.User} owning the list's id.
     * @param shelfNames    A {@link List} of names of {@link ar.edu.itba.paw.webapp.model.Shelf} to apply filtering.
     * @param statuses      A {@link List} of {@link PlayStatus} to apply filtering.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<GameWithUserShelvesWrapper> getGameList(long userId, List<String> shelfNames, List<PlayStatus> statuses,
                                                 int pageNumber, int pageSize,
                                                 UserDao.ListGameSortingType sortingType, SortDirection sortDirection);

    /**
     * Indicates whether a {@link ar.edu.itba.paw.webapp.model.Game}
     * belongs to the list of a given {@link ar.edu.itba.paw.webapp.model.User}.
     *
     * @param userId The id of the {@link ar.edu.itba.paw.webapp.model.User} to check.
     * @param gameId The id of the {@link ar.edu.itba.paw.webapp.model.Game} to check.
     * @return {@code true} if the game belongs to the list, or {@code false} otherwise.
     */
    boolean belongsToGameList(final long userId, final long gameId);
}
