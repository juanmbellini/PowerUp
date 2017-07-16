package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 27/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class GameHibernateDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    GameDao gameDao;

    /**
     * Contains genres for testing.
     */
    private final List<Game> games;
    /**
     * Contains a specific game.
     */
    private final Game chronoTrigger;
    /**
     * Contains a specific platform.
     */
    private final Platform superNintendo;
    /**
     * Contains a specific genre.
     */
    private final Genre rpg;
    /**
     * Contains a specific genre.
     */
    private final Genre platformer;
    /**
     * Contains a game with no data loaded.
     */
    private final Game unloadedDataGame;
    /**
     * Contains a user.
     */
    private final User dummyUser;

    public GameHibernateDaoTest() {
        games = new LinkedList<>();
        dummyUser = new User("paw@paw.com", "paw", "password");
        dummyUser.addAuthority(Authority.USER);
        superNintendo = new Platform(1, "Super Nintendo");
        Platform nintendo64 = new Platform(2, "Nintendo 64");
        rpg = new Genre(1, "Role Playing Game");
        platformer = new Genre(2, "Platformer");
        Genre action = new Genre(3, "Action");
        Genre adventure = new Genre(4, "Adventure");
        Genre party = new Genre(5, "Party");
        Genre fighting = new Genre(6, "Fighting");
        Company squareEnix = new Company(1, "Square-Enix");
        Keyword k1 = new Keyword(1, "rpg");
        Keyword k2 = new Keyword(2, "role playing");


        chronoTrigger = new Game.GameBuilder()
                .setName("Chrono Trigger")
                .setReleaseDate(LocalDate.of(1995, 3, 11))
                .setAvgScore(10.0)
                .addGenre(rpg)
                .addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1995, 3, 11)))
                .addDeveloper(squareEnix)
                .addPublisher(squareEnix)
                .addKeyword(k1)
                .addKeyword(k2)
                .build();
        games.add(chronoTrigger);

        games.add(new Game.GameBuilder()
                .setName("Final Fantasy VI")
                .setAvgScore(9.50)
                .setReleaseDate(LocalDate.of(1994, 4, 2))
                .addGenre(rpg)
                .addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1994, 4, 2)))
                .addKeyword(k1)
                .addKeyword(k2)
                .build());

        games.add(new Game.GameBuilder()
                .setName("Super Mario World")
                .setAvgScore(6.5)
                .setReleaseDate(LocalDate.of(1990, 11, 21))
                .addGenre(platformer)
                .addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1990, 11, 21)))
                .build());

        games.add(new Game.GameBuilder()
                .setName("Donkey Kong Country")
                .setAvgScore(7.0)
                .setReleaseDate(LocalDate.of(1994, 11, 24))
                .addGenre(platformer)
                .addPlatform(superNintendo, new GamePlatformReleaseDate(LocalDate.of(1994, 11, 24)))
                .build());

        games.add(new Game.GameBuilder()
                .setName("The Legend of Zelda: Majora's Mask")
                .setAvgScore(8.0)
                .setReleaseDate(LocalDate.of(1998, 11, 21))
                .addGenre(action)
                .addGenre(adventure)
                .addPlatform(nintendo64, new GamePlatformReleaseDate(LocalDate.of(1998, 11, 21)))
                .build());

        games.add(new Game.GameBuilder()
                .setName("Mario Party")
                .setAvgScore(6.0)
                .setReleaseDate(LocalDate.of(1999, 2, 8))
                .addGenre(party)
                .addPlatform(nintendo64, new GamePlatformReleaseDate(LocalDate.of(1999, 2, 8)))
                .build());

        games.add(new Game.GameBuilder()
                .setName("Super Smash Bros.")
                .setAvgScore(7.5)
                .setReleaseDate(LocalDate.of(1999, 1, 21))
                .addGenre(fighting)
                .addPlatform(nintendo64, new GamePlatformReleaseDate(LocalDate.of(1999, 1, 21)))
                .build());

        games.add(new Game.GameBuilder()
                .setName("Super Mario 3D Land")
                .setAvgScore(7.5)
                .setReleaseDate(LocalDate.of(2011, 11, 3))
                .addGenre(platformer)
                .addPlatform(new Platform(3, "Nintendo 3DS"), new GamePlatformReleaseDate(LocalDate.of(1999, 1, 21)))
                .build());

        unloadedDataGame = new Game.GameBuilder()
                .setName("Cat Mario")
                .setAvgScore(1)
                .setReleaseDate(LocalDate.of(1942, 1, 1))
                .build();
    }

    @Before
    public void initializeDataBase() {
        for (Game each : games) {
            each.getGenres().forEach(em::merge); // merge genres
            each.getPlatforms().keySet().forEach(em::merge); // merge platforms
            each.getPublishers().forEach(em::merge); // merge publishers
            each.getDevelopers().forEach(em::merge); // merge developers
            each.getKeywords().forEach(em::merge); // merge keywords
            em.persist(each); // Persist game
        }
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from games");
        em.createNativeQuery("delete from genres");
        em.createNativeQuery("delete from platforms");
        em.createNativeQuery("delete from developers");
        em.createNativeQuery("delete from publishers");
        em.createNativeQuery("delete from companies");
        em.createNativeQuery("delete from keywords");
        em.flush();
    }


    @Test
    public void testEmptySearch() {
        String message = "Search without filters didn't return as expected.";

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, games.size(), result.size());
        for (Game each : games) {
            Assert.assertTrue(message, result.contains(each));
        }
    }

    @Test
    public void testEmptySearchOrderedByNameAscending() {
        String message = "Search without filters didn't return as expected when ordering by name.";

        List<Game> gamesOrderedByName = new LinkedList<>(games);
        gamesOrderedByName.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByName.size(), result.size());
        Iterator listIterator = gamesOrderedByName.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchOrderedByNameDescending() {
        String message = "Search without filters didn't return as expected when ordering by name.";

        List<Game> gamesOrderedByName = new LinkedList<>(games);
        gamesOrderedByName.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, false);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByName.size(), result.size());
        Iterator listIterator = gamesOrderedByName.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchOrderedByReleaseDateAscending() {
        String message = "Search without filters didn't return as expected when ordering by release date.";

        List<Game> gamesOrderedByReleaseDate = new LinkedList<>(games);
        gamesOrderedByReleaseDate.sort((o1, o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.RELEASE, true);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByReleaseDate.size(), result.size());
        Iterator listIterator = gamesOrderedByReleaseDate.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchOrderedByReleaseDateDescending() {
        String message = "Search without filters didn't return as expected when ordering by release date.";

        List<Game> gamesOrderedByReleaseDate = new LinkedList<>(games);
        gamesOrderedByReleaseDate.sort((o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.RELEASE, false);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByReleaseDate.size(), result.size());
        Iterator listIterator = gamesOrderedByReleaseDate.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchOrderedByAvgScoreAscending() {
        String message = "Search without filters didn't return as expected when ordering by average score.";

        List<Game> gamesOrderedByAvgScore = new LinkedList<>(games);
        gamesOrderedByAvgScore.sort((o1, o2) -> Double.compare(o1.getAvgScore(), o2.getAvgScore()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.AVG_SCORE, true);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByAvgScore.size(), result.size());
        Iterator listIterator = gamesOrderedByAvgScore.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchOrderedByAvgScoreDescending() {
        String message = "Search without filters didn't return as expected when ordering by average score.";

        List<Game> gamesOrderedByAvgScore = new LinkedList<>(games);
        gamesOrderedByAvgScore.sort((o1, o2) -> Double.compare(o2.getAvgScore(), o1.getAvgScore()));

        final Collection<Game> result = gameDao.searchGames("", new HashMap<>(), OrderCategory.AVG_SCORE, false);
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, gamesOrderedByAvgScore.size(), result.size());
        Iterator listIterator = gamesOrderedByAvgScore.iterator();
        Iterator resultIterator = result.iterator();
        while (listIterator.hasNext() && resultIterator.hasNext()) {
            Assert.assertEquals(message, listIterator.next(), resultIterator.next());
        }
    }

    @Test
    public void testEmptySearchWithPagination() {
        String message = "Search without filters with pagination didn't return as expected.";

        // To check pages order, search is done applying name ascending order
        List<Game> gamesOrderedByName = new LinkedList<>(games);
        gamesOrderedByName.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        Iterator<Game> listIterator = gamesOrderedByName.iterator();
        int pageSize = 3;
        int totalPages = (gamesOrderedByName.size() % pageSize) == 0 ?
                gamesOrderedByName.size() / pageSize
                : (gamesOrderedByName.size() / pageSize) + 1;

        Page<Game> firstPage = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, pageSize, 1);
        Assert.assertNotNull(message, firstPage);
        Assert.assertEquals(message, pageSize, firstPage.getPageSize());
        Assert.assertEquals(message, 3, firstPage.getAmountOfElements());
        Assert.assertEquals(message, totalPages, firstPage.getTotalPages());
        Assert.assertEquals(message, (long) gamesOrderedByName.size(), firstPage.getOverAllAmountOfElements());
        firstPage.getData().forEach(game -> Assert.assertEquals(message, listIterator.next(), game));

        Page<Game> secondPage = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, pageSize, 2);
        Assert.assertNotNull(message, secondPage);
        Assert.assertEquals(message, pageSize, secondPage.getPageSize());
        Assert.assertEquals(message, 3, secondPage.getAmountOfElements());
        Assert.assertEquals(message, totalPages, secondPage.getTotalPages());
        Assert.assertEquals(message, (long) gamesOrderedByName.size(), secondPage.getOverAllAmountOfElements());
        secondPage.getData().forEach(game -> Assert.assertEquals(message, listIterator.next(), game));

        Page<Game> thirdPage = gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, pageSize, 3);
        Assert.assertNotNull(message, thirdPage);
        Assert.assertEquals(message, pageSize, thirdPage.getPageSize());
        Assert.assertEquals(message, 2, thirdPage.getAmountOfElements());
        Assert.assertEquals(message, totalPages, thirdPage.getTotalPages());
        Assert.assertEquals(message, (long) gamesOrderedByName.size(), thirdPage.getOverAllAmountOfElements());
        thirdPage.getData().forEach(game -> Assert.assertEquals(message, listIterator.next(), game));
    }

    @Test
    public void testSimpleFilter() {
        String message = "Search using one simple filter didn't return as expected";

        Map<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> platforms = new LinkedList<>();
        platforms.add(superNintendo.getName());
        filters.put(FilterCategory.platform, platforms);
        long count = games.stream().filter(each -> each.getPlatforms().containsKey(superNintendo)).count();

        Collection<Game> result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, count, result.size());
        games.stream().filter(game -> game.getPlatforms().containsKey(superNintendo))
                .forEach(game -> Assert.assertTrue(message, result.contains(game)));

    }

    @Test
    public void testMultipleSameKindFilters() {
        String message = "Search using several filters of the same kind didn't return as expected";

        Map<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> genres = new LinkedList<>();
        genres.add(rpg.getName());
        genres.add(platformer.getName());
        filters.put(FilterCategory.genre, genres);
        long count = games.stream()
                .filter(each -> each.getGenres().contains(rpg) || each.getGenres().contains(platformer)).count();

        Collection<Game> result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, count, result.size());
        games.stream().filter(game -> game.getGenres().contains(rpg) || game.getGenres().contains(platformer))
                .forEach(game -> Assert.assertTrue(message, result.contains(game)));

    }

    @Test
    public void testMultipleDifferentKindFilters() {
        String message = "Search using several filters of different kind didn't return as expected";

        Map<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> genres = new LinkedList<>();
        genres.add(rpg.getName());
        genres.add(platformer.getName());
        filters.put(FilterCategory.genre, genres);
        List<String> platforms = new LinkedList<>();
        platforms.add(superNintendo.getName());
        filters.put(FilterCategory.platform, platforms);

        long count = games.stream()
                .filter(each ->  (each.getGenres().contains(rpg) || each.getGenres().contains(platformer))
                        && each.getPlatforms().containsKey(superNintendo))
                .count();

        Collection<Game> result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, count, result.size());
        games.stream()
                .filter(each -> (each.getGenres().contains(rpg) || each.getGenres().contains(platformer))
                        && each.getPlatforms().containsKey(superNintendo))
                .forEach(game -> Assert.assertTrue(message, result.contains(game)));
    }

    @Test
    public void testCompaniesFilters() {
        String message = "Search using companies as filters didn't return as expected";

        Map<FilterCategory, List<String>> filters = new HashMap<>();
        List<String> companies = new LinkedList<>();
        Collection<Game> result;

        // Test just publishers
        companies.addAll(chronoTrigger.getPublishers().stream().map(Company::getName).collect(Collectors.toList()));
        filters.put(FilterCategory.publisher, companies);
        result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, 1, result.size());
        Assert.assertTrue(message, result.contains(chronoTrigger));

        //Test just developers
        companies.clear();
        filters.clear();
        companies.addAll(chronoTrigger.getDevelopers().stream().map(Company::getName).collect(Collectors.toList()));
        filters.put(FilterCategory.developer, companies);
        result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, 1, result.size());
        Assert.assertTrue(message, result.contains(chronoTrigger));

        // Test with both developers and publishers
        companies.addAll(chronoTrigger.getPublishers().stream().map(Company::getName).collect(Collectors.toList()));
        filters.put(FilterCategory.publisher, companies);
        result = gameDao.searchGames("", filters, OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, 1, result.size());
        Assert.assertTrue(message, result.contains(chronoTrigger));

    }

    @Test
    public void testEmptyResult() {
        String message = "Search using a name that doesn't exist didn't return as expected";
        Collection<Game> result = gameDao.searchGames("This name doesn't exists",
                new HashMap<>(), OrderCategory.NAME, true);

        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAsNameThrowsException(){
        gameDao.searchGames(null, new HashMap<>(), null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAsAMapForFiltersThrowsException() {
        gameDao.searchGames("", null, OrderCategory.NAME, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAsOrderCategoryThrowsException() {
        gameDao.searchGames("", new HashMap<>(), null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueForPageSizeThrowsException() {
        gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroForPageNumberThrowsException() {
        gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueForPageNumberThrowsException() {
        gameDao.searchGames("", new HashMap<>(), OrderCategory.NAME, true, 1, -1);
    }

    @Test
    public void testFindByIdWithExistingGame() {
        String message = "Find by id didn't return as expected when searching an existing game";
        Game result = gameDao.findById(chronoTrigger.getId());

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger, result);

    }

    @Test
    public void testFindByIdWithNonExistingGame() {
        String message = "Find by id didn't return as expected when searching a non existing game";
        Set<Long> ids = new HashSet<>();
        games.forEach(each -> ids.add(each.getId()));
        long id = Collections.max(ids) + 1; // The next one to the biggest.

        Assert.assertNull(message, gameDao.findById(id));
    }


    @Test
    public void testFindByIdsWithExistingGames() {
        String message = "Find by ids didn't return as expected when searching games that exist";

        List<Long> evenIds = games.stream().map(Game::getId).filter(each -> each % 2 == 0).collect(Collectors.toList());
        List<Long> oddIds = games.stream().map(Game::getId).filter(each -> each % 2 == 1).collect(Collectors.toList());

        Map<Long, Game> result = gameDao.findByIds(evenIds);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, evenIds.size(), result.keySet().size());
        evenIds.forEach(each -> Assert.assertTrue(message, result.containsKey(each)));
        oddIds.forEach(each -> Assert.assertFalse(message, result.containsKey(each)));
        result.keySet().forEach(each -> Assert.assertEquals(message, each.longValue(), result.get(each).getId()));

    }

    @Test
    public void findByIdsWithExistingAndNonExistingGames() {
        String message = "Find by ids didn't return as expected when searching existing and non existing games";

        List<Long> ids = games.stream().map(Game::getId).filter(each -> each % 2 == 0).collect(Collectors.toList());
        // Add a Non existing id
        ids.add(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);

        Map<Long, Game> result = gameDao.findByIds(ids);

        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, ids.size(), result.keySet().size() + 1);
        result.keySet().forEach(each -> Assert.assertTrue(ids.contains(each)));
        result.keySet().forEach(each -> Assert.assertEquals(message, each.longValue(), result.get(each).getId()));

    }

    @Test
    public void findByIdsWithNonExistingGames() {
        String message = "Find by ids didn't return as expected when searching games that doesn't exist";

        long nonExistingId = Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1;
        List<Long> ids = new ArrayList<>();
        ids.add(nonExistingId++);
        ids.add(nonExistingId++);
        ids.add(nonExistingId++);
        ids.add(nonExistingId);

        Map<Long, Game> result = gameDao.findByIds(ids);

        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());

    }

    @Test
    public void testExistsWithId() {
        String message = "Exists with id didn't return as expected";

        long nonExistingId = Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1;
        Assert.assertTrue(message, gameDao.existsWithId(chronoTrigger.getId()));
        Assert.assertFalse(message, gameDao.existsWithId(nonExistingId));
    }

   @Test
    public void testExistsWithTitle() {
       String message = "Exists with title didn't return as expected";

       Assert.assertTrue(message, gameDao.existsWithTitle(chronoTrigger.getName()));
       Assert.assertFalse(message, gameDao.existsWithTitle("This title doesn't exist"));
    }

    @Test
    public void testGetGenresWithExistingGame() {
        String message = "Get genres didn't return as expected using a game that exists";

        Collection<Genre> result = gameDao.getGenres(chronoTrigger.getId());
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger.getGenres().size(), result.size());
        chronoTrigger.getGenres().forEach(each -> Assert.assertTrue(message, result.contains(each)));

    }

    @Test
    public void testGetGenresToGameWithNoGenresLoaded() {
        String message = "Get genres didn't return as expected when using a game with no genres loaded";
        em.persist(unloadedDataGame);
        em.flush();

        Collection<Genre> result = gameDao.getGenres(unloadedDataGame.getId());
        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test (expected = NoSuchEntityException.class)
    public void testGetGenresWithNonExistingGame() {
        gameDao.getGenres(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);
    }

    @Test
    public void testGetPlatformWithExistingGame() {
        String message = "Get platform didn't return as expected using a game that exists";

        Map<Platform, GamePlatformReleaseDate> result = gameDao.getPlatforms(chronoTrigger.getId());
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger.getPlatforms().size(), result.size());
        chronoTrigger.getPlatforms().keySet().forEach(each -> Assert.assertTrue(message, result.containsKey(each)));

    }

    @Test
    public void testGetPlatformsToGameWithNoPlatformLoaded() {
        String message = "Get platforms didn't return as expected when using a game with no platforms loaded";
        em.persist(unloadedDataGame);
        em.flush();

        Map<Platform, GamePlatformReleaseDate> result = gameDao.getPlatforms(unloadedDataGame.getId());
        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test (expected = NoSuchEntityException.class)
    public void testGetPlatformWithNonExistingGame() {
        gameDao.getPlatforms(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);
    }

    @Test
    public void testGetDevelopersWithExistingGame() {
        String message = "Get developers didn't return as expected using a game that exists";

        Collection<Company> result = gameDao.getDevelopers(chronoTrigger.getId());
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger.getDevelopers().size(), result.size());
        chronoTrigger.getDevelopers().forEach(each -> Assert.assertTrue(message, result.contains(each)));

    }

    @Test
    public void testGetDevelopersToGameWithNoDevelopersLoaded() {
        String message = "Get developers didn't return as expected when using a game with no developers loaded";
        em.persist(unloadedDataGame);
        em.flush();

        Collection<Company> result = gameDao.getDevelopers(unloadedDataGame.getId());
        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test (expected = NoSuchEntityException.class)
    public void testGetDevelopersWithNonExistingGame() {
        gameDao.getGenres(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);
    }

    @Test
    public void testGetPublishersWithExistingGame() {
        String message = "Get publishers didn't return as expected using a game that exists";

        Collection<Company> result = gameDao.getPublishers(chronoTrigger.getId());
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger.getPublishers().size(), result.size());
        chronoTrigger.getPublishers().forEach(each -> Assert.assertTrue(message, result.contains(each)));

    }

    @Test
    public void testGetPublishersToGameWithNoPublishersLoaded() {
        String message = "Get publishers didn't return as expected when using a game with no publishers loaded";
        em.persist(unloadedDataGame);
        em.flush();

        Collection<Company> result = gameDao.getPublishers(unloadedDataGame.getId());
        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test (expected = NoSuchEntityException.class)
    public void testGetPublishersWithNonExistingGame() {
        gameDao.getGenres(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);
    }

    @Test
    public void testGetKeywordsWithExistingGame() {
        String message = "Get keywords didn't return as expected using a game that exists";

        Collection<Keyword> result = gameDao.getKeywords(chronoTrigger.getId());
        Assert.assertNotNull(message, result);
        Assert.assertEquals(message, chronoTrigger.getKeywords().size(), result.size());
        chronoTrigger.getKeywords().forEach(each -> Assert.assertTrue(message, result.contains(each)));

    }

    @Test
    public void testGetKeywordsToGameWithNoKeywordsLoaded() {
        String message = "Get keywords didn't return as expected when using a game with no keywords loaded";
        em.persist(unloadedDataGame);
        em.flush();

        Collection<Keyword> result = gameDao.getKeywords(unloadedDataGame.getId());
        Assert.assertNotNull(message, result);
        Assert.assertTrue(message, result.isEmpty());
    }

    @Test (expected = NoSuchEntityException.class)
    public void testGetKeywordsWithNonExistingGame() {
        gameDao.getKeywords(Collections.max(games.stream().map(Game::getId).collect(Collectors.toList())) + 1);
    }

//    @Test
//    public void testUpdateAvgScoreToSameValue() {
//        String message = "Update avg score didn't return as expected";
//
//        double initialAvgScore = chronoTrigger.getAvgScore();
//        gameDao.updateAvgScore(c);
//    }




}