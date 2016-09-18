package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.model.Filter;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static junit.framework.TestCase.assertNotNull;


/**
 * Created by dgrimau on 14/09/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class GameJdbcDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private GameJdbcDao gameDao;

    private JdbcTemplate jdbcTemplate;

    private void inicializeDataBase() {

        String insert = "BEGIN;\n" +
                "SET DATESTYLE TO ISO, YMD;\n" +
                "\\encoding utf8;\n" +
                "\n" +
                "--Genres\n" +
                "INSERT INTO power_up.genres (\"id\", \"name\") VALUES (1, 'Platformer');\n" +
                "INSERT INTO power_up.genres (\"id\", \"name\") VALUES (2, 'Action');\n" +
                "INSERT INTO power_up.genres (\"id\", \"name\") VALUES (3, 'Party Game');\n" +
                "\n" +
                "INSERT INTO power_up.platforms (\"id\", \"name\") VALUES (2, 'SEGA');\n" +
                "INSERT INTO power_up.platforms (\"id\", \"name\") VALUES (1, 'Nintendo 64');\n" +
                "INSERT INTO power_up.platforms (\"id\", \"name\") VALUES (3, 'Nintendo GameCube');\n" +
                "\n" +
                "INSERT INTO power_up.keywords (\"id\", \"name\") VALUES (1, 'Fun');\n" +
                "INSERT INTO power_up.keywords (\"id\", \"name\") VALUES (2, 'Action');\n" +
                "INSERT INTO power_up.keywords (\"id\", \"name\") VALUES (3, 'Party');\n" +
                "\n" +
                "INSERT INTO power_up.companies (\"id\", \"name\") VALUES (1, 'Nintendo');\n" +
                "INSERT INTO power_up.companies (\"id\", \"name\") VALUES (2, 'SEGA');\n" +
                "INSERT INTO power_up.companies (\"id\", \"name\") VALUES (2, 'Nintendo Party');\n" +
                "\n" +
                "INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');\n" +
                "INSERT INTO power_up.games VALUES (2, 'Super Mario Party', '', 0, '2018-12-30');\n" +
                "INSERT INTO power_up.games VALUES (3, 'Sonic', 'SANIC.', 0, '2018-12-30');\n" +
                "\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (1, 2) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (2, 3) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_keywords (game_id, keyword_id) VALUES (3, 2) ON CONFLICT DO NOTHING;\n" +

                "\n" +
                "INSERT INTO power_up.game_platforms (game_id, console_id, release_date) VALUES (1, 1, '2018-12-30') ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_platforms (game_id, console_id, release_date) VALUES (1, 3, '2018-12-30') ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_platforms (game_id, console_id, release_date) VALUES (2, 1, '2018-12-30') ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_platforms (game_id, console_id, release_date) VALUES (3, 2, '2018-12-30') ON CONFLICT DO NOTHING;\n" +

                "\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (1, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (2, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_publishers (game_id, publisher_id) VALUES (3, 2) ON CONFLICT DO NOTHING;\n" +
                "\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (1, 2) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (2, 3) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_genres (game_id, genre_id) VALUES (3, 1) ON CONFLICT DO NOTHING;\n" +
                "\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (1, 1) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (2, 3) ON CONFLICT DO NOTHING;\n" +
                "INSERT INTO power_up.game_developers (game_id, developer_id) VALUES (3, 1) ON CONFLICT DO NOTHING;\n" +
                "\n" +
                "\n" +
                "COMMIT;";
        jdbcTemplate.execute(insert);
    }

    //TODO afterClass to clean DB


    @Before
    public void setUp() {
        jdbcTemplate = gameDao.getJdbcTemplate();

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.games", "power_up.platforms", "power_up.game_platforms",
                "power_up.game_developers",  " power_up.game_genres ",  "power_up.game_publishers", "power_up.game_keywords",
                "power_up.companies", "power_up.keywords", "power_up.genres");

    }

    @Test
    public void testSimpleSearchFound() {

        //SetUp db with three games. "Mario", "Super Mario Party" and "Sonic"


        //
        final Collection<Game> games = gameDao.searchGame("Mario",new HashSet()); //testear null y collection vacia

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
        HashSet filters = new HashSet();
        Filter genreFilter = new Filter(Filter.FilterCategory.GENRES, "Platformer");
        filters.add(genreFilter);
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
        HashSet filters = new HashSet();
        Filter firstKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Fun");
        Filter secondeKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Action");
        filters.add(firstKeywordFilter);
        filters.add(secondeKeywordFilter);
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
        HashSet filters = new HashSet();
        Filter firstKeywordFilter = new Filter(Filter.FilterCategory.KEYWORDS, "Action");
        Filter firstGenreFilter = new Filter(Filter.FilterCategory.GENRES, "Platformer");
        Filter firstPlatformFilter = new Filter(Filter.FilterCategory.PLATFORMS, "Nintendo 64");
        filters.add(firstKeywordFilter);
        filters.add(firstGenreFilter);
        filters.add(firstPlatformFilter);

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
            HashSet filters = new HashSet();
            Filter developerFilter = new Filter(Filter.FilterCategory.DEVELOPERS, "Nintendo");
            Filter publisherFilter = new Filter(Filter.FilterCategory.PUBLISHERS, "Nintendo");

            filters.add(developerFilter);
            filters.add(publisherFilter);
            final Collection<Game> games = gameDao.searchGame("Mario",filters); //testear null y collection vacia

            assertNotNull(games);
            assert(games.size()==1);

            Iterator<Game> iterator = games.iterator();
            Game game = iterator.next();

            assert(game.getName().equals("Mario"));





    }


}





