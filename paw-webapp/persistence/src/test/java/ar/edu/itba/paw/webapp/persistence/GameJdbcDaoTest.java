package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.junit.Test;
import ar.edu.itba.paw.webapp.model.Game;

import javax.sql.DataSource;

import java.util.*;

import static junit.framework.TestCase.assertNotNull;


/**
 * Created by dgrimau on 14/09/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class GameJdbcDaoTest {


    @Autowired
    private GameJdbcDao gameDao;

    private JdbcTemplate jdbcTemplate;

    private void inicializeDataBase() {

        String insert =
//                "BEGIN;\n" +
//                "SET DATESTYLE TO ISO, YMD;\n" +
//                "\\encoding utf8;\n" +
//                "\n" +
                "--Genres\n" +
//                        "SET IDENTITY_INSERT power_up.genres ON"+
//                        "SET IDENTITY_INSERT power_up.platforms ON"+
//                        "SET IDENTITY_INSERT power_up.keywords ON"+
//                        "SET IDENTITY_INSERT power_up.companies ON"+
//                        "SET IDENTITY_INSERT power_up.games ON"+
//                        "SET IDENTITY_INSERT power_up.game_genres ON"+
//                        "SET IDENTITY_INSERT power_up.game_keywords ON"+
//                        "SET IDENTITY_INSERT power_up.game_publisher ON"+
//                        "SET IDENTITY_INSERT power_up.game_developer ON"+
//                        "SET IDENTITY_INSERT power_up.game_platforms ON"+

                "INSERT INTO power_up.genres (id, name) VALUES (1, 'Platformer');\n" +
                "INSERT INTO power_up.genres (id, name) VALUES (2, 'Action');\n" +
                "INSERT INTO power_up.genres (id, name) VALUES (3, 'Party Game');\n" +
                "\n" +
                "INSERT INTO power_up.platforms (id, name) VALUES (2, 'SEGA');\n" +
                "INSERT INTO power_up.platforms (id, name) VALUES (1, 'Nintendo 64');\n" +
                "INSERT INTO power_up.platforms (id, name) VALUES (3, 'Nintendo GameCube');\n" +
                "\n" +
                "INSERT INTO power_up.keywords (id, name) VALUES (1, 'Fun');\n" +
                "INSERT INTO power_up.keywords (id, name) VALUES (2, 'Action');\n" +
                "INSERT INTO power_up.keywords (id, name) VALUES (3, 'Party');\n" +
                "\n" +
                "INSERT INTO power_up.companies (id, name) VALUES (1, 'Nintendo');\n" +
                "INSERT INTO power_up.companies (id, name) VALUES (2, 'SEGA');\n" +
                "INSERT INTO power_up.companies (id, name) VALUES (2, 'Nintendo Party');\n" +
                "\n" +
                "INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');\n" +
                "INSERT INTO power_up.games VALUES (2, 'Super Mario Party', '', 0, '2018-12-30');\n" +
                "INSERT INTO power_up.games VALUES (3, 'Sonic', 'SANIC.', 0, '2018-12-30');\n" +
                "\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 1);\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 2);\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 1);\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 3);\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 1);\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 2);\n" +

                "\n" +
                "INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (1, 1, '2018-12-30');\n" +
                "INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (1, 3, '2018-12-30');\n" +
                "INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (2, 1, '2018-12-30');\n" +
                "INSERT INTO power_up.game_platforms (game_id, platform_id, release_date) VALUES (3, 2, '2018-12-30');\n" +

                "\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (1, 1);\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (2, 1);\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (3, 2);\n" +
                "\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 1);\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 2);\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (2, 3);\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (3, 1);\n" +
                "\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (1, 1);\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (2, 3);\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (3, 1);\n" +
                "\n" +
                "\n"
//                "COMMIT;"
                ;
        jdbcTemplate.execute(insert);
    }

    //TODO afterClass to clean DB


    @Before
    public void setUp() {
        jdbcTemplate = gameDao.getJdbcTemplate();

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.games", "power_up.platforms", "power_up.game_platforms",
                "power_up.game_developers",  " power_up.game_genres ",  "power_up.game_publishers", "power_up.game_keywords",
                "power_up.companies", "power_up.keywords", "power_up.genres");

        inicializeDataBase();
    }

    @Test
    public void testSimpleSearchFound() {

        //SetUp db with three games. "Mario", "Super Mario Party" and "Sonic"


        //
        final Collection<Game> games = gameDao.searchGame("Mario",new HashMap()); //testear null y collection vacia

        assertNotNull(games);
        assert(games.size()==2);

        Iterator<Game> iterator = games.iterator();
        Game firstGame = iterator.next();
        Game secondGame = iterator.next();

        assert((firstGame.getName().equals("Mario") || secondGame.getName().equals("Mario")) && !(firstGame.getName().equals("Mario") && secondGame.getName().equals("Mario")));

        //TODO testear que se cargue bien el summary y eso????
    }

    @Test
    public void testSimpleFilter(){
        //SetUp db with three games. "Mario" with genre "Platformer, Action", "Super Mario Party" with genre "Party Game" and "Sonic with genre "Platformer""

        //
        HashMap filters = new HashMap();
        List filterListGenre = new ArrayList<>();
        filterListGenre.add("Platformer");
        filters.put(FilterCategory.genre,filterListGenre);
//        Filter genreFilter = new Filter(FilterCategory.GENRES, "Platformer");
//        filters.add(genreFilter);
        final Collection<Game> games = gameDao.searchGame("Mario",filters); //testear null y collection vacia

        assertNotNull(games);
        assert(games.size()==1);

        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert(game.getName().equals("Mario"));

    }

    @Test
    public void testMultipleSameKindFilters(){
        //SetUp db with three games. "Mario" keyword "Fun, Action", "Super Mario Party" keyword "Fun", "Sonic" keyword "Platformer, Fun" and "Mario Golf" keyword "Golf, MegaFun"

        //
        HashMap filters = new HashMap();
        List filterListKeyword = new ArrayList<>();
        filterListKeyword.add("Fun");
        filterListKeyword.add("Action");
        filters.put(FilterCategory.keyword,filterListKeyword);

       // HashSet filters = new HashSet();
//        Filter firstKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Fun");
//        Filter secondeKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Action");
//        filters.add(firstKeywordFilter);
//        filters.add(secondeKeywordFilter);
        final Collection<Game> games = gameDao.searchGame("Mario",filters); //testear null y collection vacia

        assertNotNull(games);
        assert(games.size()==1);

        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert(game.getName().equals("Mario"));

    }

    @Test
    public void testMultipleDifferentKindFilters(){
        //SetUp db with three games. "Mario" keyword "Fun, Action" genre "Platformer, Action" platform "Nintendo 64, Nintendo GameCube"
        // , "Super Mario Action Party" keyword "Fun, Action"  genre "Party" platform "Nintendo 64"
        // , "Sonic" keyword "Platformer, Fun", genre "Platformer", platform "SEGA"

        //

//        HashSet filters = new HashSet();
//        Filter firstKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Action");
//        Filter firstGenreFilter = new Filter(Filter.FilterCategory.GENRES, "Platformer");
//        Filter firstPlatformFilter = new Filter(Filter.FilterCategory.PLATFORMS, "Nintendo 64");
//        filters.add(firstKeywordFilter);
//        filters.add(firstGenreFilter);
//        filters.add(firstPlatformFilter);

        HashMap filters = new HashMap();
        List filterListKeyword = new ArrayList<>();
        filterListKeyword.add("Action");
        filters.put(FilterCategory.keyword,filterListKeyword);

        List filterListGenre = new ArrayList();
        filterListGenre.add("Platformer");
        filters.put(FilterCategory.genre, filterListGenre);

        List filterListPlatform = new ArrayList();
        filterListGenre.add("Nintendo 64");
        filters.put(FilterCategory.platform, filterListPlatform);



        final Collection<Game> games = gameDao.searchGame("Mario",filters); //testear null y collection vacia

        assertNotNull(games);
        assert(games.size()==1);


        Iterator<Game> iterator = games.iterator();
        Game game = iterator.next();

        assert(game.getName().equals("Mario"));
    }

    @Test
    public void testCompaniesFilters(){

            //SetUp db with three games. "Mario" with genre "Platformer, Action", publisher Nintendo, developper Nintendo
        // "Super Mario Party" with genre "Party Game" publsher Nintendo publisher GolfStation
        // and "Sonic with genre "Platformer" developper Nintendo publisher Sega"

            //
//            HashSet filters = new HashSet();
//            Filter developerFilter = new Filter(Filter.FilterCategory.DEVELOPERS, "Nintendo");
//            Filter publisherFilter = new Filter(Filter.FilterCategory.PUBLISHERS, "Nintendo");
//
//            filters.add(developerFilter);
//            filters.add(publisherFilter);

        HashMap filters = new HashMap();
        List filterListPublisher = new ArrayList<>();
        filterListPublisher.add("Nintendo");
        filters.put(FilterCategory.publisher,filterListPublisher);

        List filterListDeveloper = new ArrayList();
        filterListDeveloper.add("Nintendo");
        filters.put(FilterCategory.developer, filterListDeveloper);

            final Collection<Game> games = gameDao.searchGame("Mario",filters); //testear null y collection vacia

            assertNotNull(games);
            assert(games.size()==1);

            Iterator<Game> iterator = games.iterator();
            Game game = iterator.next();

            assert(game.getName().equals("Mario"));





    }


}





