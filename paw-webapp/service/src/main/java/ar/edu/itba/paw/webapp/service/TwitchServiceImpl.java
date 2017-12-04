package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.TwitchClient;
import ar.edu.itba.paw.webapp.interfaces.TwitchService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.TwitchStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Concrete implementation of {@link TwitchService}.
 */
@Service
@Transactional(readOnly = true)
public class TwitchServiceImpl implements TwitchService {

    /**
     * A {@link TwitchClient} to operate with Twitch through their API.
     */
    private final TwitchClient twitchClient;

    /**
     * A {@link GameDao} to fetch a {@link Game} by its id.
     */
    private final GameDao gameDao;


    @Autowired
    public TwitchServiceImpl(TwitchClient twitchClient, GameDao gameDao) {
        this.twitchClient = twitchClient;
        this.gameDao = gameDao;
    }


    @Override
    public List<TwitchStream> getStreamsByGameId(long gameId) {
        return Optional.ofNullable(gameDao.findById(gameId))
                .map(Game::getName)
                .map(twitchClient::getStreamsByName)
                .orElseThrow(NoSuchEntityException::new);
    }
}
