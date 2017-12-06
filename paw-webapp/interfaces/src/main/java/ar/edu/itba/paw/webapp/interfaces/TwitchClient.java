package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.TwitchStream;

import java.util.List;

/**
 * Defines behaviour for an object that can perform actions in Twitch through their API
 */
public interface TwitchClient {

    /**
     * Returns a {@link List} of {@link TwitchStream}, for a given game.
     *
     * @param gameName The name of the game of the streams to be returned.
     * @return A {@link List} containing the top 25 {@link TwitchStream} returned by the Twitch API.
     */
    List<TwitchStream> getStreamsByName(String gameName);
}
