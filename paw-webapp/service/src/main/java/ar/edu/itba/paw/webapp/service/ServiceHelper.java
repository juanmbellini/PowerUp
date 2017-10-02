package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class implementing logic that can be shared among all services.
 */
/* package */ class ServiceHelper {

    /**
     * Creates a new {@link Page} from the given {@code oldPage}, applying the given {@code transformFunction}.
     *
     * @param oldPage           The old {@link Page} from which data is taken.
     * @param transformFunction The {@link Function} that is applied to each element of the {@code oldPage}
     *                          in order to create the new {@link Page}
     * @param <E>               The type of elements the {@code oldPage} holds.
     * @param <T>               The type of elements the new {@link Page} will hold.
     * @return The new {@link Page}.
     */
    /* package */
    static <E, T> Page.Builder<T> fromAnotherPage(Page<E> oldPage, Function<E, T> transformFunction) {
        return new Page.Builder<T>()
                .setTotalPages(oldPage.getTotalPages())
                .setOverAllAmountOfElements(oldPage.getOverAllAmountOfElements())
                .setPageSize(oldPage.getPageSize())
                .setPageNumber(oldPage.getPageNumber())
                .setData(oldPage.getData().stream().map(transformFunction).collect(Collectors.toList()));
    }
}
