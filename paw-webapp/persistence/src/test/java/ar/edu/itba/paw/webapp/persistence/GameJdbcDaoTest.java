package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;


/**
 * Created by dgrimau on 14/09/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class GameJdbcDaoTest {

    //TODO avoid copy-pasting
    private static final String DEFAULT_PICTURE_URL = "https://4.bp.blogspot.com/-9wibpF5Phs0/VubnbJfiprI/AAAAAAAABYg/TVSE7O7-yGYr_gCoBlObBc6DRve90LoIw/s1600/image06.png";

    @Autowired
    private GameJdbcDao gameDao;

    private JdbcTemplate jdbcTemplate;

    private void initializeDataBase() {

        StringBuilder insert = new StringBuilder().append("")
                // Inserts genres
                .append("INSERT INTO power_up.genres (id, name) VALUES (1, 'Platformer');\n")
                .append("INSERT INTO power_up.genres (id, name) VALUES (2, 'Action');\n")
                .append("INSERT INTO power_up.genres (id, name) VALUES (3, 'Party Game');\n")
                // Inserts platforms
                .append("INSERT INTO power_up.platforms (id, name) VALUES (2, 'SEGA');\n")
                .append("INSERT INTO power_up.platforms (id, name) VALUES (1, 'Nintendo 64');\n")
                .append("INSERT INTO power_up.platforms (id, name) VALUES (3, 'Nintendo GameCube');\n")
                //* Inserts keywords
                .append("INSERT INTO power_up.keywords (id, name) VALUES (1, 'Fun');\n")
                .append("INSERT INTO power_up.keywords (id, name) VALUES (2, 'Action');\n")
                .append("INSERT INTO power_up.keywords (id, name) VALUES (3, 'Party');\n")
                // Inserts companies
                .append("INSERT INTO power_up.companies (id, name) VALUES (1, 'Nintendo');\n")
                .append("INSERT INTO power_up.companies (id, name) VALUES (2, 'SEGA');\n")
                .append("INSERT INTO power_up.companies (id, name) VALUES (3, 'Nintendo Party');\n")
                // Inserts games
                .append("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');\n")
                .append("INSERT INTO power_up.games VALUES (2, 'Super Mario Party', '', 0, '2018-12-30');\n")
                .append("INSERT INTO power_up.games VALUES (3, 'Sonic', 'SANIC.', 0, '2018-12-30');\n")
                .append("INSERT INTO power_up.games VALUES (4, 'Megaman I', 'Megaman .', 0, '2000-12-30');\n")
                .append("INSERT INTO power_up.games VALUES (5, 'Megaman II', '', 0, '2012-12-30');\n")
                .append("INSERT INTO power_up.games VALUES (6, 'Megaman III', 'Megaman!!', 0, '2014-12-30');\n")

                // Inserts game-keywords relationship
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 1);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 2);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 1);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 3);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 1);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 2);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (4, 2);\n")
                .append("INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (5, 2);\n")
                // Inserts game-platforms relationship
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (1, 1, '1998-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (1, 3, '2018-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (2, 1, '2018-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (3, 2, '2018-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (4, 2, '2018-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (5, 2, '2018-12-30');\n")
                .append("INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (6, 2, '2018-12-30');\n")
                // Inserts game-publishers relationship
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (1, 1);\n")
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (2, 1);\n")
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (3, 2);\n")
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (4, 2);\n")
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (5, 2);\n")
                .append("INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (6, 2);\n")
                // Inserts game-genres relationship
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 1);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 2);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (2, 3);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (3, 1);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (4, 2);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (5, 2);\n")
                .append("INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (6, 2);\n")
                // Inserts game-developers relationship
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (1, 1);\n")
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (2, 3);\n")
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (3, 1);\n")
                // Inserts game-images relationship
                .append("INSERT INTO power_up.game_pictures (game_id, cloudinary_id, width, height)")
                .append("VALUES(1, 'whgrfj9muktnnpags6qg', 1280, 720);\n")
                .append("INSERT INTO power_up.game_pictures (game_id, cloudinary_id, width, height)")
                .append("VALUES(2, 'fouukgohwdwhusnx05dx', 1920, 1080);\n")
                .append("INSERT INTO power_up.game_pictures (game_id, cloudinary_id, width, height)")
                .append("VALUES(1, 'vacodos9raqxrtibmsnc', 2560, 1440);")
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (4, 1);\n")
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (5, 1);\n")
                .append("INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (6, 1);\n");
        jdbcTemplate.execute(insert.toString());
    }

    //TODO afterClass to clean DB


    @Before
    public void setUp() {
        jdbcTemplate = gameDao.getJdbcTemplate();
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.games", "power_up.platforms", "power_up.game_platforms",
                "power_up.game_developers", " power_up.game_genres ", "power_up.game_publishers", "power_up.game_keywords",
                "power_up.companies", "power_up.keywords", "power_up.genres");
        initializeDataBase();
    }


    @Test
    public void testEmptySearch() {
        final Collection<Game> games = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true);
        assertNotNull(games);
        assertEquals("Search without filters didn't return as expected.", 6, games.size());
    }

    @Test
    public void testSimpleSearchFound() {
        final Collection<Game> games = gameDao.searchGames("Mario", new HashMap<>(), OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search without filters didn't return as expected.", 2, games.size());

        Iterator<Game> iterator = games.iterator();
        Game firstGame = iterator.next();
        Game secondGame = iterator.next();

        assert ((firstGame.getName().equals("Mario") || secondGame.getName().equals("Mario")) && !(firstGame.getName().equals("Mario") && secondGame.getName().equals("Mario")));

        //TODO testear que se cargue bien el summary y eso????
    }

    @Test
    public void testSimpleFilter() {
        System.out.println("Performing simple filter test...");

        HashMap<FilterCategory, List<String>> filters = new HashMap<>();
        List filterListGenre = new ArrayList<>();
        filterListGenre.add("Action");
        filters.put(FilterCategory.genre, filterListGenre);

        final Collection<Game> games = gameDao.searchGames("Mario", filters, OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search with one filter didn't return as expected.", 1, games.size());

        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert (game.getName().equals("Mario"));

    }

    @Test
    public void testMultipleSameKindFilters() {
        System.out.println("Performing multiple same kind filters test...");

        HashMap<FilterCategory, List<String>> filters = new HashMap();
        List<String> filterListKeyword = new ArrayList<>();
        filterListKeyword.add("Fun");
        filterListKeyword.add("Action");
        filters.put(FilterCategory.keyword, filterListKeyword);

        final Collection<Game> games = gameDao.searchGames("", filters, OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search with multiple filters of the same kind didn't return as expected.", 5, games.size());

        final Collection<Game> gamesMario = gameDao.searchGames("Mario", filters, OrderCategory.name, true);
        assertNotNull(gamesMario);
        assertEquals("Search with multiple filters of the same kind didn't return as expected when using parameter 'name'.", 2, gamesMario.size());

    }

    @Test
    public void testMultipleDifferentKindFilters() {
        System.out.println("Performing multiple different filters test...");

        HashMap<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> filterListKeyword = new ArrayList<>();
        filterListKeyword.add("Action");
        filters.put(FilterCategory.keyword, filterListKeyword);

        List<String> filterListGenre = new ArrayList<>();
        filterListGenre.add("Platformer");
        filters.put(FilterCategory.genre, filterListGenre);

        List<String> filterListPlatform = new ArrayList<>();
        filterListPlatform.add("Nintendo 64");
        filters.put(FilterCategory.platform, filterListPlatform);


        final Collection<Game> games = gameDao.searchGames("Mario", filters, OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search with multiple filters of different kind didn't return as expected.", 1, games.size());


        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert (game.getName().equals("Mario"));
    }

    @Test
    public void testCompaniesFiltersSameCompanies() {
        System.out.println("Performing companies filter test...");

        HashMap<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> filterListPublisher = new ArrayList<>();
        filterListPublisher.add("Nintendo");
        filters.put(FilterCategory.publisher, filterListPublisher);

        List<String> filterListDeveloper = new ArrayList<>();
        filterListDeveloper.add("Nintendo");
        filters.put(FilterCategory.developer, filterListDeveloper);

        final Collection<Game> games = gameDao.searchGames("Mario", filters, OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search using Publisher and Developer filter didn't return as expected.", 1, games.size());


        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert (game.getName().equals("Mario"));


    }

    @Test
    public void testCompaniesFiltersDifferentCompanies() {
        System.out.println("Performing companies filter test...");

        HashMap<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> filterListPublisher = new ArrayList<>();
        filterListPublisher.add("SEGA");
        filters.put(FilterCategory.publisher, filterListPublisher);

        List<String> filterListDeveloper = new ArrayList<>();
        filterListDeveloper.add("Nintendo");
        filters.put(FilterCategory.developer, filterListDeveloper);

        final Collection<Game> games = gameDao.searchGames("Megaman", filters, OrderCategory.name, true);

        assertNotNull(games);
        assertEquals("Search using Publisher and Developer filter didn't return as expected.", 3, games.size());

    }

    @Test
    public void testFindNonExistingGameById() {
        System.out.println("Performing find game by id test when game isn't in database...");

        Game unknownGame = gameDao.findById(4000);
        assertNull("Find by id didn't return as expected. Returned Game with id 4000, expected null", unknownGame);

    }

    @Test
    public void testFindGameById() {
        System.out.println("Performing find game by id test...");

        Game marioParty = gameDao.findById(2);
        Game chronoTrigger = gameDao.findById(3);


        assertNotNull("Find by id didn't return as expected. Returned null, expected Game with id 2", marioParty);
        assertEquals("Find by id didn't return as expected. " +
                "Returned Game with id " + marioParty.getId() + ", expected Game with id 2", 2, marioParty.getId());
        assertEquals("Find by id didn't return as expected. " +
                        "Returned Game with name " + marioParty.getName() + ", expected Game with name 'Super Mario Party'",
                "Super Mario Party", marioParty.getName());


        assertNotNull("Find by id didn't return as expected. Returned null, expected Game with id 3", chronoTrigger);
        assertEquals("Find by id didn't return as expected. " +
                        "Returned Game with id " + chronoTrigger.getId() + ", expected Game with id 3",
                3, chronoTrigger.getId());
        assertNotEquals("Find by id did't return as expected. " +
                        "Returned Game with name 'Chrono Trigger', expected Game with different name",
                "Chrono Trigger", chronoTrigger.getName());

    }

    @Test
    public void testGetPlatformReleaseDate() {
        System.out.println("Performing get platform release date...");
        Game game = gameDao.findById(1);

        assertNotNull(game.getPlatforms());
        assertTrue("Platform Nintendo 64 should exist for game " + game.getName(), game.getPlatforms().containsKey("Nintendo 64"));
        assertEquals(game.getPlatforms().get("Nintendo 64").getYear(), 1998);
        assertEquals(game.getPlatforms().get("Nintendo 64").getMonthOfYear(), 12);
        assertEquals(game.getPlatforms().get("Nintendo 64").getDayOfMonth(), 30);

    }


    @Test
    public void testGetFiltersByType() {
        System.out.println("Performing get filters by type test...");

        // Tests keywords filter (simple filter)
        Collection<String> keywordFilters = gameDao.getFiltersByType(FilterCategory.keyword);
        assertNotNull("Get filters by Type didn't returned as expected. " +
                "Returned null, expected a Collection", keywordFilters);
        assertEquals("Get filters by Type didn't returned as expected. " +
                        "Returned a Collection with " + keywordFilters.size() + " elements, expected 3",
                3, keywordFilters.size());
        assertTrue("Get filters by Type didn't returned as expected. " +
                "Resultant collection didn't contained 'Fun' as a keyword", keywordFilters.contains("Fun"));
        assertTrue("Get filters by Type didn't returned as expected. " +
                "Resultant collection didn't contained 'Action' as a keyword", keywordFilters.contains("Action"));
        assertTrue("Get filters by Type didn't returned as expected. " +
                "Resultant collection didn't contained 'Party' as a keyword", keywordFilters.contains("Party"));
        assertFalse("Get filters by Type didn't returned as expected. " +
                "Resultant collection contained 'Shooter' as a keyword", keywordFilters.contains("Shooter"));


    }


    @Test
    public void testGetFiltersByTypeForCompaniesFilters() {
        System.out.println("Performing get filters by type test on companies filters...");

        // Tests publishers filters (mapped to companies table)
        Collection<String> publishersFilters = gameDao.getFiltersByType(FilterCategory.publisher);
        assertNotNull("Get filters by Type didn't returned as expected for publishers. " +
                "Returned null, expected a Collection", publishersFilters);
        assertEquals("Get filters by Type didn't returned as expected for publishers. " +
                        "Returned a Collection with " + publishersFilters.size() + " elements, expected 2",
                2, publishersFilters.size());
        assertTrue("Get filters by Type didn't returned as expected for publishers. " +
                        "Resultant collection didn't contained 'Nintendo' as a publisher",
                publishersFilters.contains("Nintendo"));
        assertTrue("Get filters by Type didn't returned as expected for publishers. " +
                        "Resultant collection didn't contained 'SEGA' as a publisher",
                publishersFilters.contains("SEGA"));
        assertFalse("Get filters by Type didn't returned as expected for publishers. " +
                        "Resultant collection contained 'Nintendo Party' as a publisher",
                publishersFilters.contains("Nintendo Party"));

        // Tests developers filters (mapped to companies table)
        Collection<String> developersFilters = gameDao.getFiltersByType(FilterCategory.developer);
        assertNotNull("Get filters by Type didn't returned as expected for developers. " +
                "Returned null, expected a Collection", developersFilters);
        assertEquals("Get filters by Type didn't returned as expected for developers. " +
                        "Returned a Collection with " + developersFilters.size() + " elements, expected 2",
                2, developersFilters.size());
        assertTrue("Get filters by Type didn't returned as expected for developers. " +
                        "Resultant collection didn't contained 'Nintendo' as a developer",
                developersFilters.contains("Nintendo"));
        assertTrue("Get filters by Type didn't returned as expected for developers. " +
                        "Resultant collection didn't contained 'Nintendo Party' as a developer",
                developersFilters.contains("Nintendo Party"));
        assertFalse("Get filters by Type didn't returned as expected for developers. " +
                        "Resultant collection contained 'SEGA' as a developer",
                developersFilters.contains("SEGA"));


    }

    @Test
    public void testGetCoverPicture() {
        System.out.println("Performing get cover picture test...");
        Game gameSinglePicture = gameDao.findById(2);

        assertEquals(buildUrl("fouukgohwdwhusnx05dx"), gameSinglePicture.getCoverPictureUrl());

        assertNotNull(gameSinglePicture.getPictureUrls());
        assertEquals(1, gameSinglePicture.getPictureUrls().size());
        assertTrue(gameSinglePicture.getPictureUrls().contains(buildUrl("fouukgohwdwhusnx05dx")));
    }

    private String buildUrl(String s) {
        return "https://res.cloudinary.com/igdb/image/upload/t_cover_big_2x/" + s + ".jpg";
    }


    @Test
    public void testGetMultiplePictures() {
        Game gameMultiplePictures = gameDao.findById(1);

        assertNotNull(gameMultiplePictures.getPictureUrls());
        assertEquals(2, gameMultiplePictures.getPictureUrls().size());
        assertTrue(gameMultiplePictures.getPictureUrls().contains(buildUrl("whgrfj9muktnnpags6qg")));
        assertTrue(gameMultiplePictures.getPictureUrls().contains(gameMultiplePictures.getCoverPictureUrl()));
    }

    @Test
    public void TestGetNullPicture() {
        System.out.println("Performing get null picture test...");
        Game game = gameDao.findById(3);

        assertNotNull(game.getPictureUrls());
        assertEquals(DEFAULT_PICTURE_URL, game.getCoverPictureUrl());

    }


    @Test
    public void TestOrderByName() {

        final Collection<Game> gameCollection = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true);
        Game oldGame = null;
        assertEquals(6, gameCollection.size());
        for (Game game : gameCollection) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue((oldGame.getName().compareTo(game.getName()) <= 0));
            }
        }

        final Collection<Game> gameCollectionDesc = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, false);
        oldGame = null;
        for (Game game : gameCollectionDesc) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue((oldGame.getName().compareTo(game.getName()) >= 0));
            }
        }
    }


    @Test
    public void TestOrderByAvgScore() {

        final Collection<Game> gameCollection = gameDao.searchGames("", new HashMap<>(), OrderCategory.avg_score, true);
        Game oldGame = null;
        assertEquals(6, gameCollection.size());
        for (Game game : gameCollection) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue(oldGame.getAvgScore() <= game.getAvgScore());
            }
        }

        final Collection<Game> gameCollectionDesc = gameDao.searchGames("", new HashMap<>(), OrderCategory.avg_score, false);
        oldGame = null;
        for (Game game : gameCollectionDesc) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue(oldGame.getAvgScore() >= game.getAvgScore());
            }
        }

    }


    @Test
    public void TestOrderByRelease() {

        final Collection<Game> gameCollection = gameDao.searchGames("", new HashMap<>(), OrderCategory.release, true);
        Game oldGame = null;
        assertEquals(6, gameCollection.size());
        for (Game game : gameCollection) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue((oldGame.getReleaseDate().compareTo(game.getReleaseDate()) <= 0));
            }
        }

        final Collection<Game> gameCollectionDesc = gameDao.searchGames("", new HashMap<>(), OrderCategory.release, false);
        oldGame = null;
        for (Game game : gameCollectionDesc) {
            assertNotNull(game);
            if (oldGame == null) oldGame = game;
            else {
                assertTrue((oldGame.getReleaseDate().compareTo(game.getReleaseDate()) >= 0));
            }
        }

    }

    @Test
    public void testFindRelatedGamesWithOneSimpleFilter() {
        System.out.println("Performing find related games test using one simple filter (i.e. keywords)...");

        final Game mario = gameDao.findById(1);
        final Game marioParty = gameDao.findById(2);
        final Game sonic = gameDao.findById(3);
        final Game megaManIII = gameDao.findById(6);

        final Set<FilterCategory> filters = new HashSet<>();
        filters.add(FilterCategory.keyword);
        final Set<Game> relatedToMario = gameDao.findRelatedGames(mario, filters);

        Assert.assertNotNull("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games, got null",
                relatedToMario);
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Game Mario, " +
                        "got a Set including it.",
                relatedToMario.contains(mario));
        assertTrue("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games containing Game Sonic, " +
                        "got a Set that didn't include it.",
                relatedToMario.contains(sonic));
        assertTrue("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that include Mario Party," +
                        "got a Set that included it.",
                relatedToMario.contains(marioParty));

        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include megaManIII," +
                        "got a Set that included it.",
                relatedToMario.contains(megaManIII));

    }

    @Test
    public void testFindRelatedGamesWithOneComplexFilter() {
        System.out.println("Performing find related games test twice using one complex filters each time " +
                "(i.e. publishers and developers)...");
        System.out.println("Starting with publishers...");

        final Game mario = gameDao.findById(1);
        final Game marioParty = gameDao.findById(2);
        final Game sonic = gameDao.findById(3);
        final Set<FilterCategory> filters = new HashSet<>();

        // Tests method using publishers as filter
        filters.add(FilterCategory.publisher);
        Set<Game> relatedToMario = gameDao.findRelatedGames(mario, filters);

        Assert.assertNotNull("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games, got null",
                relatedToMario);
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Game Mario, " +
                        "got a Set including it.",
                relatedToMario.contains(mario));
        assertTrue("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games containing Game Sonic, " +
                        "got a Set that didn't include it.",
                relatedToMario.contains(marioParty));
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Mario Party," +
                        "got a Set that included it.",
                relatedToMario.contains(sonic));


        System.out.println("Continuing with developers...");
        filters.clear();

        // Tests method using developers as filter
        filters.add(FilterCategory.developer);
        relatedToMario = gameDao.findRelatedGames(mario, filters);
        Assert.assertNotNull("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games, got null",
                relatedToMario);
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Game Mario, " +
                        "got a Set including it.",
                relatedToMario.contains(mario));
        assertTrue("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games containing Game Sonic, " +
                        "got a Set that didn't include it.",
                relatedToMario.contains(sonic));
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Mario Party," +
                        "got a Set that included it.",
                relatedToMario.contains(marioParty));
    }


    @Test
    public void testFindRelatedGamesWithMoreThanOneFilter() {
        System.out.println("Performing find related games test using more than one filter " +
                "(i.e. genres and developers)...");

        final Game mario = gameDao.findById(1);
        final Game marioParty = gameDao.findById(2);
        final Game sonic = gameDao.findById(3);
        final Set<FilterCategory> filters = new HashSet<>();
        filters.add(FilterCategory.keyword);
        filters.add(FilterCategory.developer);
        Set<Game> relatedToMario = gameDao.findRelatedGames(mario, filters);

        Assert.assertNotNull("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games, got null",
                relatedToMario);
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Game Mario, " +
                        "got a Set including it.",
                relatedToMario.contains(mario));
        assertTrue("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games containing Game Sonic, " +
                        "got a Set that didn't include it.",
                relatedToMario.contains(sonic));
        assertFalse("Find related gamed didn't returned as expected. " +
                        "Expected a Set of Games that doesn't include Mario Party," +
                        "got a Set that included it.",
                relatedToMario.contains(marioParty));
    }

    @Test
    public void testPaginationWithRowsCountBeingMultipleOfPageSize() {
        String baseResultString = "Search Games with pagination didn't returned as expected.";

        Page<Game> bigPage = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 6, 1);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null", bigPage);
        assertEquals(baseResultString + " Expected a page with 6 elements, got " + bigPage.getData().size(),
                6, bigPage.getData().size());
        assertEquals(baseResultString + " Expected one page, got " + bigPage.getTotalPages(),
                1, bigPage.getTotalPages());

        Page<Game> mediumPage1 = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 3, 1);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null", mediumPage1);
        assertEquals(baseResultString + " Expected a page with 3 elements, got " + mediumPage1.getData().size(),
                3, mediumPage1.getData().size());
        assertEquals(baseResultString + " Expected 2 pages, got " + mediumPage1.getTotalPages(),
                2, mediumPage1.getTotalPages());
        Page<Game> mediumPage2 = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 3, 2);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null", mediumPage2);
        assertEquals(baseResultString + " Expected a page with 3 elements, got " + mediumPage1.getData().size(),
                3, mediumPage1.getData().size());
        assertEquals(baseResultString + " Expected 2 pages, got " + mediumPage2.getTotalPages(),
                2, mediumPage2.getTotalPages());

        for (Game gameFromPage1 : mediumPage1.getData()) {
            Assert.assertNotNull(baseResultString + " There shouldn't be null objects in the page", gameFromPage1);
            for (Game gameFromPage2 : mediumPage2.getData()) {
                Assert.assertNotNull(baseResultString + " There shouldn't be null objects in the page", gameFromPage2);
                assertNotEquals(baseResultString + " Expected different games in different pages",
                        gameFromPage1, gameFromPage2);
            }
        }
        Iterator<Game> it = mediumPage1.getData().iterator();
        Game game = it.next();
        assertEquals(baseResultString + " First game should be Mario, got " + game.getName(), 1, game.getId());
        game = it.next();
        assertEquals(baseResultString + " First game should be Megaman I, got " + game.getName(), 4, game.getId());
        game = it.next();
        assertEquals(baseResultString + " First game should be Megaman II, got " + game.getName(), 5, game.getId());
    }

    @Test(expected = IllegalPageException.class)
    public void testWrongPaginationArguments() {
        try {
            gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 6, 2);
            fail("Search Games with pagination didn't returned as expected. " +
                    "Expected a IllegalPageException to be thrown");
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Test
    public void testPaginationWithSmallerPageSizeThanRowsCountAndWithoutBeingRowsCountMultipleOfPageSize() {
        String baseResultString = "Search Games with pagination didn't returned as expected.";

        Page<Game> page1 = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 5, 1);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null.", page1);
        assertEquals(baseResultString + " Expected a page with 5 elements, got " + page1.getData().size(),
                5, page1.getData().size());
        assertFalse(baseResultString + " Expected a page not containing 'Super Mario Party', got a page containing it",
                page1.getData().contains(new Game(2, "", "")));
        assertEquals(baseResultString + " Expected 2 pages, got " + page1.getTotalPages(),
                2, page1.getTotalPages());

        Page<Game> page2 = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 5, 2);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null.", page2);
        assertEquals(baseResultString + " Expected a page with 5 elements, got " + page2.getData().size(),
                1, page2.getData().size());
        assertEquals(baseResultString + " Expected a page of size 1, got a page of size " + page2.getPageSize(),
                1, page2.getPageSize());
        assertTrue(baseResultString + " Expected a page containing 'Super Mario Party', got a page not containing it",
                page2.getData().contains(new Game(2, "", "")));
        assertEquals(baseResultString + " Expected 2 pages, got " + page2.getTotalPages(),
                2, page2.getTotalPages());

    }

    @Test
    public void testPaginationWithBiggerPageSizeThanRowsCount() {
        String baseResultString = "Search Games with pagination didn't returned as expected.";

        Page<Game> page = gameDao.searchGames("", new HashMap<>(), OrderCategory.name, true, 7, 1);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null.", page);
        assertEquals(baseResultString + " Expected a page with 6 elements, got " + page.getData().size(),
                6, page.getData().size());
        assertEquals(baseResultString + " Expected a page of size 6, got a page of size " + page.getPageSize(),
                6, page.getPageSize());
        assertEquals(baseResultString + " Expected one page, got " + page.getTotalPages(),
                1, page.getTotalPages());
    }

    @Test
    public void testPaginationWithEmptyResultSet() {
        String baseResultString = "Search Games with pagination didn't returned as expected.";

        Page<Game> page = gameDao.searchGames("asdasdasdasd", new HashMap<>(), OrderCategory.name, true, 5, 1);
        Assert.assertNotNull(baseResultString + " Expected a page of games, got null.", page);
        assertEquals(baseResultString + " Expected a page with no elements, got " + page.getData().size(),
                0, page.getData().size());
        assertEquals(baseResultString + " Expected a page of size 0, got a page of size " + page.getPageSize(),
                0, page.getPageSize());
        assertEquals(baseResultString + " Expected one page, got " + page.getTotalPages(),
                1, page.getTotalPages());
    }

}





