package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.model.Filter;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
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
public class GameJdbcDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private GameJdbcDao gameDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.games");
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
        assert(game.getGenres().size()==2);
        assert(game.getGenres().get(0).equals("Platformer") || game.getGenres().get(1).equals("Platformer"));

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
        assert(game.getKeywords().size()==2);
        assert(game.getKeywords().get(0).equals("Fun") || game.getKeywords().get(1).equals("Fun"));

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

            //SetUp db with three games. "Mario" with genre "Platformer, Action", "Super Mario Party" with genre "Party Game" and "Sonic with genre "Platformer""

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
            assert(game.getGenres().size()==2);
            assert(game.getGenres().get(0).equals("Platformer") || game.getGenres().get(1).equals("Platformer"));




    }


}





