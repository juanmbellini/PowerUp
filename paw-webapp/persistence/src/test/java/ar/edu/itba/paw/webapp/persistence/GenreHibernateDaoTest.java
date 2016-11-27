package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import org.junit.After;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 26/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class GenreHibernateDaoTest {


    /**
     * Used to persist testing data.
     */
    @PersistenceContext
    protected EntityManager em;


    @Autowired
    private GenreHibernateDao genreDao;


    /**
     * Contains genres for testing
     */
    private final List<Genre> genres;
    /**
     * Constains a specific company
     */
    private final Genre platformer;


    public GenreHibernateDaoTest() {
        this.genres = new LinkedList<>();
        this.platformer = new Genre(0, "Platformer");
        genres.add(platformer);
        genres.add(new Genre(0, "RPF"));
        genres.add(new Genre(0, "Action"));
    }

    @Before
    public void initializeDatabase() {
        genres.forEach(em::persist);
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from genres");
        em.flush();
    }


    @Test
    public void testAllGenresAreReturned() {
        String message = "Get all genres didn't return as expected";
        Set<Genre> returnedGenres = genreDao.all();

        Assert.assertNotNull(message, returnedGenres);
        Assert.assertEquals(message, genres.size(), returnedGenres.size());
        for (Genre each : genres) {
            Assert.assertTrue(message, returnedGenres.contains(each));
        }
    }

    @Test
    public void testFindById() {
        String message = "Find by id didn't return as expected";
        Genre returnedGenre = genreDao.findById(platformer.getId());

        Assert.assertNotNull(message, returnedGenre);
        Assert.assertEquals(message, platformer.getId(), returnedGenre.getId());
        Assert.assertEquals(message, platformer.getName(), returnedGenre.getName());
    }

    @Test
    public void testGamesWithGenre() {
        String message = "Games with genre didn't return as expected";

        List<Game> platformerGames = new LinkedList<>();
        platformerGames.add(new Game.GameBuilder().setName("Super Mario Bros.").addGenre(platformer).build());
        platformerGames.add(new Game.GameBuilder().setName("Donkey Kong Country 2").addGenre(platformer).build());
        platformerGames.forEach(em::persist);
        platformer.setGames(platformerGames);
        em.merge(platformer);
        em.flush();

        Set<Game> returnedGames = genreDao.gamesWithGenre(platformer);

        Assert.assertNotNull(message, returnedGames);
        Assert.assertEquals(message, platformerGames.size(), returnedGames.size());
        for (Game each : platformerGames) {
            Assert.assertTrue(message, returnedGames.contains(each));
        }

        em.createNativeQuery("delete from games");
        em.flush();

    }

}
