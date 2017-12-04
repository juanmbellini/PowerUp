package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.TwitchStream;

import java.util.List;

/**
 * Defines behaviour for an object that can provide Twitch services.
 */
public interface TwitchService {

    /**
     * Retrieves a {@link List} with the top 25 {@link TwitchStream}s
     * of a given {@link ar.edu.itba.paw.webapp.model.Game}
     *
     * @param gameId The id of the {@link ar.edu.itba.paw.webapp.model.Game} which the {@link TwitchStream} belongs to.
     * @return The {@link List} of {@link TwitchStream}.
     */
    List<TwitchStream> getStreamsByGameId(long gameId);
}
