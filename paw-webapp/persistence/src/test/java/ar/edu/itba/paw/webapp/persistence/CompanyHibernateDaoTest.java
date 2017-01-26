package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
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
import java.util.*;

/**
 * Created by Juan Marcos Bellini on 26/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class CompanyHibernateDaoTest {

    /**
     * Used to persist testing data.
     */
    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private CompanyHibernateDao companyDao;

    /**
     * Contains companies for testing
     */
    private final List<Company> companies;

    /**
     * Contains a specific company
     */
    private final Company nintendo;

    public CompanyHibernateDaoTest() {
        companies = new LinkedList<>();
        nintendo = new Company(0, "Nintendo");
        companies.add(nintendo);
        companies.add(new Company(0, "Square-Enix"));
    }


    @Before
    public void initializeDatabase() {
        companies.forEach(em::persist);
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from companies");
        em.flush();
    }


    @Test
    public void testAllCompaniesAreReturned() {
        String message = "Get all companies didn't return as expected.";
        List<Company> returnedCompanies = companyDao.all();

        Assert.assertNotNull(message, returnedCompanies);
        Assert.assertEquals(message, this.companies.size(), returnedCompanies.size());
        for (Company each : companies) {
            Assert.assertTrue(message, returnedCompanies.contains(each));
        }

    }

    @Test
    public void testFindById() {
        String message = "Find by id didn't return as expected.";
        Company returnedCompany = companyDao.findById(nintendo.getId());

        Assert.assertNotNull(message, returnedCompany);
        Assert.assertEquals(message, nintendo.getId(), returnedCompany.getId());
        Assert.assertEquals(message, nintendo.getName(), returnedCompany.getName());
    }

    @Test
    public void testFindByName() {
        String message = "Find by name didn't return as expected.";
        Company returnedCompany = companyDao.findByName(nintendo.getName());

        Assert.assertNotNull(message, returnedCompany);
        Assert.assertEquals(message, nintendo.getId(), returnedCompany.getId());
        Assert.assertEquals(message, nintendo.getName(), returnedCompany.getName());
    }

    @Test
    public void testGamesDevelopedBy() {
        String message = "Games developed by didn't return as expected.";

        Company rareware = createRarewareCompanyWithTwoGames();
        Collection<Game> developedByRare = rareware.getGamesDeveloped();
        Set<Game> returnedGamesDeveloped = companyDao.gamesDevelopedBy(rareware);

        Assert.assertNotNull(message, returnedGamesDeveloped);
        Assert.assertEquals(message, developedByRare.size(), returnedGamesDeveloped.size());
        for (Game each : developedByRare) {
            Assert.assertTrue(message, returnedGamesDeveloped.contains(each));
        }

        // Remove games that were just persisted
        deleteGames();
    }


    @Test
    public void testGamesPublishedBy() {
        String message = "Games published by didn't return as expected.";

        setGamesToNintendo();
        Collection<Game> publishedByNintendo = nintendo.getGamesPublished();
        Set<Game> returnedGamesPublished = companyDao.gamesPublishedBy(nintendo);

        Assert.assertNotNull(message, returnedGamesPublished);
        Assert.assertEquals(message, publishedByNintendo.size(), returnedGamesPublished.size());
        for (Game each : publishedByNintendo) {
            Assert.assertTrue(message, returnedGamesPublished.contains(each));
        }

        // Remove games that were just persisted
        deleteGames();
    }

    /**
     * Creates a {@link Company} (with name Rareware), setting two games as developed games.
     * This method persists those games and the company. After that it flushes data into the database.
     * Note: deleteGames method should be called after in order to clean the database.
     *
     * @return A new Company with two games in its developed games set.
     */
    private Company createRarewareCompanyWithTwoGames() {
        Company rareware = new Company(0, "Rareware");
        List<Game> developedByRare = new LinkedList<>();
        developedByRare.add(new Game.GameBuilder()
                .setName("Donkey Kong Country 1")
                .addDeveloper(rareware)
                .build());
        developedByRare.add(new Game.GameBuilder()
                .setName("Donkey Kong 64")
                .addDeveloper(rareware)
                .build());
        developedByRare.forEach(em::persist);

        rareware.setGamesDeveloped(developedByRare);
        em.persist(rareware);
        em.flush();

        return rareware;
    }

    /**
     * Sets two games into the Nintendo {@link Company}.
     * This method persists those games, and merges the company. After that it flushes data into the database.
     * Note: deleteGames method should be called after in order to clean the database.
     */
    private void setGamesToNintendo() {
        List<Game> publishedByNintendo = new LinkedList<>();
        publishedByNintendo.add(new Game.GameBuilder()
                .setName("Donkey Kong Country 2")
                .addPublisher(nintendo)
                .build());
        publishedByNintendo.add(new Game.GameBuilder()
                .setName("The Legend of Zelda: A Link to the Past")
                .addPublisher(nintendo)
                .build());
        publishedByNintendo.forEach(em::persist);

        nintendo.setGamesPublished(publishedByNintendo);
        em.merge(nintendo);
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
