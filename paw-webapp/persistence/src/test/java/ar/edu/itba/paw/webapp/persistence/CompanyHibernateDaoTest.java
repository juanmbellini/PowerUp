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
public class CompanyHibernateDaoTest {

    @Autowired
    private CompanyHibernateDao companyDao;

    @Autowired
    private GameHibernateDao gameDao;

    /**
     * Contains companies for testing
     */
    private final Set<Company> companies;

    /**
     * Contains a specific company
     */
    private final Company nintendo;

    public CompanyHibernateDaoTest() {
        companies = new HashSet<>();
        nintendo = new Company(0, "Nintendo");
        companies.add(nintendo);
        companies.add(new Company(0, "Square-Enix"));
    }


    @Before
    public void initializeDatabase() {
        companies.forEach(companyDao.getEntityManager()::persist);
    }

    @After
    public void removeAllData() {
        companyDao.getEntityManager().createNativeQuery("delete from companies");
    }


    @Test
    @Transactional
    public void testAllCompaniesAreReturned() {
        String message = "Get all companies didn't returned as expected.";
        Set<Company> returnedCompanies = companyDao.all();

        Assert.assertNotNull(message, returnedCompanies);
        Assert.assertEquals(message, this.companies.size(), returnedCompanies.size());
        for (Company each : companies) {
            Assert.assertTrue(message, returnedCompanies.contains(each));
        }

    }

    @Test
    @Transactional
    public void testFindById() {
        String message = "Find by id didn't returned as expected.";
        Company returnedCompany = companyDao.findById(nintendo.getId());

        Assert.assertNotNull(message, returnedCompany);
        Assert.assertEquals(message, nintendo.getId(), returnedCompany.getId());
        Assert.assertEquals(message, nintendo.getName(), returnedCompany.getName());
    }

    @Test
    @Transactional
    public void testFindByName() {
        String message = "Find by name didn't returned as expected.";
        Company returnedCompany = companyDao.findByName(nintendo.getName());

        Assert.assertNotNull(message, returnedCompany);
        Assert.assertEquals(message, nintendo.getId(), returnedCompany.getId());
        Assert.assertEquals(message, nintendo.getName(), returnedCompany.getName());
    }

    @Test
    @Transactional
    public void testGamesDevelopedBy() {
        String message = "Games developed by didn't returned as expected.";

        Company rareware = new Company(0, "Rareware");
        List<Game> developedByRare = new LinkedList<>();
        developedByRare.add(new Game.GameBuilder().setName("Donkey Kong Country 1").build());
        developedByRare.add(new Game.GameBuilder().setName("Donkey Kong 64").build());
        developedByRare.forEach(gameDao.getEntityManager()::persist);
        rareware.setGamesDeveloped(developedByRare);

        companyDao.getEntityManager().persist(rareware);
        Set<Game> returnedGamesDeveloped = companyDao.gamesDevelopedBy(rareware);

        Assert.assertNotNull(message, returnedGamesDeveloped);
        Assert.assertEquals(message, developedByRare.size(), returnedGamesDeveloped.size());
        for (Game each : developedByRare) {
            Assert.assertTrue(message, returnedGamesDeveloped.contains(each));
        }

        // Remove games that were just persisted
        gameDao.getEntityManager().createNativeQuery("delete from games");
    }


    @Test
    @Transactional
    public void testGamesPublishedBy() {
        String message = "Games published by didn't returned as expected.";

        List<Game> publishedByNintendo = new LinkedList<>();
        publishedByNintendo.add(new Game.GameBuilder().setName("Donkey Kong Country 1").build());
        publishedByNintendo.add(new Game.GameBuilder().setName("The Legend of Zelda: A Link to the Past").build());
        publishedByNintendo.forEach(gameDao.getEntityManager()::persist);
        nintendo.setGamesPublished(publishedByNintendo);

        companyDao.getEntityManager().merge(nintendo);
        Set<Game> returnedGamesPublished = companyDao.gamesPublishedBy(nintendo);

        Assert.assertNotNull(message, returnedGamesPublished);
        Assert.assertEquals(message, publishedByNintendo.size(), returnedGamesPublished.size());
        for (Game each : publishedByNintendo) {
            Assert.assertTrue(message, returnedGamesPublished.contains(each));
        }

        // Remove games that were just persisted
        gameDao.getEntityManager().createNativeQuery("delete from games");
    }

}
