package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Publisher;

import java.util.Set;

/**
 * Data Access Object for Game Publishers
 */
public interface PublisherDao {

    /**
     * @see PublisherService#all()
     */
    Set<Publisher> all();

    /**
     * @see PublisherService#findById(long)
     */
    Publisher findById(long id);

    /**
     * @see PublisherService#findByName(String)
     */
    Publisher findByName(String name);

    /**
     * @see PublisherService#gamesPublishedBy(Publisher)
     */
    Set<Game> gamesPublishedBy(Publisher p);
}
