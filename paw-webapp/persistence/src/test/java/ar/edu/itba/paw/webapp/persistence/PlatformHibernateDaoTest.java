package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.PlatformDao;
import ar.edu.itba.paw.webapp.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 27/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class PlatformHibernateDaoTest {

    /**
     * Used to persist testing data.
     */
    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private PlatformDao platformDao;


    /**
     * Contains genres for testing
     */
    private final List<Platform> platforms;
    /**
     * Contains a specific company
     */
    private final Platform superNintendo;
    /**
     * Contains a specific game
     */
    private final Game chronoTrigger;


    public PlatformHibernateDaoTest() {
        platforms = new LinkedList<>();
        superNintendo = new Platform(0, "Super Nintendo");
        chronoTrigger = new Game.GameBuilder()
                .setName("Chrono Trigger")
                .build();
        platforms.add(superNintendo);
        platforms.add(new Platform(0, "Nintendo 64"));
        platforms.add(new Platform(0, "Sega Genesis"));
    }


    @Before
    public void initializeDatabase() {
        platforms.forEach(em::persist);
        em.flush();
    }


    public void removeData() {
        em.createNativeQuery("DELETE from platforms");
        em.flush();
    }


    @Test
    public void testAllPlatformsAreReturned() {
        String message = "Get all platforms didn't return as expected.";
        java.util.Collection<Platform> returnedGenres = platformDao.all();

        Assert.assertNotNull(message, returnedGenres);
        Assert.assertEquals(message, platforms.size(), returnedGenres.size());
        for (Platform each : platforms) {
            Assert.assertTrue(message, returnedGenres.contains(each));
        }
    }

    @Test
    public void testFindById() {
        String message = "Find by id didn't return as expected.";
        Platform returnedGenre = platformDao.findById(superNintendo.getId());

        Assert.assertNotNull(message, returnedGenre);
        Assert.assertEquals(message, superNintendo.getId(), returnedGenre.getId());
        Assert.assertEquals(message, superNintendo.getName(), returnedGenre.getName());
    }

    @Test
    public void testFindByName() {
        String message = "Find by name didn't return as expected.";
        Platform returnedCompany = platformDao.findByName(superNintendo.getName());

        Assert.assertNotNull(message, returnedCompany);
        Assert.assertEquals(message, superNintendo.getId(), returnedCompany.getId());
        Assert.assertEquals(message, superNintendo.getName(), returnedCompany.getName());
    }

    @Test
    public void testGamesReleasedFor() {
        String message = "Games released for didn't return as expected.";

        addGamesToSuperNintendo();
        Collection<Game> superNintendoGames = superNintendo.getGames();
        Set<Game> returnedGames = platformDao.gamesReleasedFor(superNintendo);

        Assert.assertNotNull(message, returnedGames);
        Assert.assertEquals(message, superNintendoGames.size(), returnedGames.size());
        for (Game each : superNintendoGames) {
            Assert.assertTrue(message, returnedGames.contains(each));
        }
        deleteGames();
    }

    @Test
    public void testReleaseDateForGame() {
        String message = "Released date for didn't return as expected.";

        LocalDate releaseDate = LocalDate.of(1995, 3, 11);
        chronoTrigger.addPlatform(superNintendo, new GamePlatformReleaseDate(releaseDate));
        em.persist(chronoTrigger);
        List<Game> auxList = new LinkedList<>();
        auxList.add(chronoTrigger);
        superNintendo.setGames(auxList);
        em.merge(superNintendo);
        em.flush();

        LocalDate returnedDate = platformDao.releaseDateForGame(chronoTrigger, superNintendo);

        Assert.assertNotNull(message, returnedDate);
        Assert.assertEquals(message, releaseDate, returnedDate);


        Platform nintendoDS = new Platform(0, "Nintendo DS");
        LocalDate releaseDateForNintendoDS = LocalDate.of(2009, 2, 6);
        chronoTrigger.addPlatform(nintendoDS, new GamePlatformReleaseDate(releaseDateForNintendoDS));
        nintendoDS.setGames(auxList);
        em.persist(nintendoDS);
        em.merge(chronoTrigger);
        em.flush();

        returnedDate = platformDao.releaseDateForGame(chronoTrigger, nintendoDS);

        Assert.assertNotNull(message, returnedDate);
        Assert.assertEquals(message, releaseDateForNintendoDS, returnedDate);

        deleteGames();

    }


    /**
     * Adds released games into the Super Nintendo {@link Platform}.
     * This method persists those games, and merges the platform. After that it flushes data into the database.
     * Note: deleteGames method should be called after in order to clean the database.
     */
    private void addGamesToSuperNintendo() {
        List<Game> superNintendoGames = new LinkedList<>();
        chronoTrigger.addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1995, 3, 11)));
        superNintendoGames.add(new Game.GameBuilder()
                .setName("Final Fantasy VI")
                .addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1995, 3, 11)))
                .build());
        superNintendoGames.forEach(em::persist);
        superNintendo.setGames(superNintendoGames);
        em.merge(superNintendo);
        em.flush();
    }

    /**
     * Removes games and flushes it into the database.
     */
    private void deleteGames() {
        em.createNativeQuery("delete from games");
        em.flush();
    }


}
