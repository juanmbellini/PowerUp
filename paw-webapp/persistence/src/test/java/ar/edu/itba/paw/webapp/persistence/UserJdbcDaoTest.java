package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserJdbcDaoTest {

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    private void insertOneGame(int id) {
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES ("+id+", 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30',null,0);");
    }

    @Before
    public void cleanUp() {

        jdbcTemplate = userDao.getJdbcTemplate();

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.users"); //TODO drop rates tables.
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "power_up.games", "power_up.platforms", "power_up.game_platforms",
                "power_up.game_developers", " power_up.game_genres ", "power_up.game_publishers", "power_up.game_keywords",
                "power_up.companies", "power_up.keywords", "power_up.genres");
    }

    @Test
    public void testFindIDWithEmptyTable() {
        assertNull("Found a user by ID when the table is empty", userDao.findById(42));
    }

    @Test
    public void testFindEmailWithEmptyTable() {
        assertNull("Found a user by email when the table is empty", userDao.findByEmail("nonexistent"));
    }

    @Test
    public void testFindUsernameWithEmptyTable() {
        assertNull("Found a user by username when the table is empty", userDao.findByUsername("nonexistent"));
    }


    @Test
    public void testFindWithNonEmptyTable() {

        String email = "email", password = "password", username = "jorge";
        int id = 1;
        jdbcTemplate.execute("INSERT INTO power_up.users (id, username, email, hashed_password) VALUES (" + id + ", '" + username + "', '" + email + "', '" + password + "' );");

        Assert.assertNotNull("Created user not found by email", userDao.findByEmail(email));
        Assert.assertNotNull("Created user not found by username", userDao.findByUsername(username));

        final User u = userDao.findById(id);

        assertEquals(u.getUsername(), username);
        assertEquals(u.getEmail(), email);
        assertEquals(u.getId(), id);
    }


    //TODO test password


    @Test
    public void testCreateWithAllInfo() {
        String email = "email";
        String username = "user";
        String password = "password";

        User u = userDao.create(email, password, username);
        Assert.assertNotNull("Created user is null", u);
        assertEquals("Mismatching created user email", u.getEmail(), email);
        assertEquals("Mismatching created user username", u.getUsername(), username);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "power_up.users"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutUsername() {
        String email = "email";
        String password = "password";

        User u = userDao.create(email, password, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutPassword() {
        String email = "email";
        String username = "user";

        User u = userDao.create(email, null, username);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutEmail() {

        String password = "password";
        String username = "user";

        User u = userDao.create(null, password, username);
    }


    @Test(expected = UserExistsException.class)
    public void testDuplicateEmail() {
        String email = "email";
        String username = "user";
        String password = "password";

        User u = userDao.create(email, password, username);
        User neverCreated = userDao.create(email, "email2", "user2");
    }

    @Test(expected = UserExistsException.class)
    public void testDuplicateUsername() {
        String email = "email";
        String username = "user";
        String password = "password";
        User u = userDao.create("email1", password, username);
        User neverCreated = userDao.create("email2", password, username);
    }

    //TODO make non-CreateTests without using the "create" method.


    @Test(expected = IllegalArgumentException.class)
    public void scoreNullGame() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);

        Assert.assertNotNull(u);
        userDao.scoreGame(u, null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreForNullUser() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        userDao.scoreGame(null, 1, 4);
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    public void scoreForNonExistingUser(){
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        userDao.scoreGame(1,1,4);
    }
    */

    @Test(expected = IllegalArgumentException.class)
    public void scoreOutOfBoundsNegative() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        userDao.scoreGame(u, 1, -1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreOutOfBoundsPositive() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        userDao.scoreGame(u, 1, 11);

    }

    @Test
    public void scoreBound() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);

        insertOneGame(id);
        userDao.scoreGame(u, 1, 1);
        userDao.scoreGame(u, 1, 10);
    }

    @Test
    public void scoreMultipleTime() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        userDao.scoreGame(u, 1, 2);

        assertEquals(2, userDao.findById(u.getId()).getGameScore(id));

        userDao.scoreGame(u, id, 5);

        userDao.scoreGame(u, id, 9);

        assertEquals(9, userDao.findById(u.getId()).getGameScore(id));

        userDao.scoreGame(u, id, 3);


        userDao.scoreGame(u, id, 4);

        assertEquals(4, userDao.findById(u.getId()).getGameScore(id));
    }

    @Test
    public void scoreWithGameParameter() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        Game g = new Game();
        g.setId(id);


        userDao.scoreGame(u, g, 2);

        assertEquals(2, userDao.findById(u.getId()).getGameScore(g.getId()));


        userDao.scoreGame(u, g, 5);

        userDao.scoreGame(u, g, 9);

        assertEquals(9, userDao.findById(u.getId()).getGameScore(g.getId()));


        userDao.scoreGame(u, g, 3);


        userDao.scoreGame(u, g, 4);

        assertEquals(4, userDao.findById(u.getId()).getGameScore(g.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScoreForNonScoredGame() {

        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        u.getGameScore(1);

    }




    @Test(expected = IllegalArgumentException.class)
    public void setPlayStatusNullGame() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);

        Assert.assertNotNull(u);
        userDao.setPlayStatus(u, null, PlayStatus.PLAN_TO_PLAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPlayStatusForNullUser() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        userDao.setPlayStatus(null, 1, PlayStatus.PLAYED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayStatusForNonGivenStatusGame() {

        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);
        u.getPlayStatus(1);

    }


    @Test
    public void setPlayStatusMultipleTime() {
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);
        insertOneGame(id);

        userDao.setPlayStatus(u, 1, PlayStatus.PLAYED);

        assertEquals(PlayStatus.PLAYED, userDao.findById(u.getId()).getPlayStatus(id));

        userDao.setPlayStatus(u, 1, PlayStatus.PLAN_TO_PLAY);

        userDao.setPlayStatus(u, 1, PlayStatus.PLAYING);

        assertEquals(PlayStatus.PLAYING, userDao.findById(u.getId()).getPlayStatus(id));

        userDao.setPlayStatus(u, 1, PlayStatus.PLAYING);

        userDao.setPlayStatus(u, 1, PlayStatus.PLAN_TO_PLAY);

        assertEquals(PlayStatus.PLAN_TO_PLAY, userDao.findById(u.getId()).getPlayStatus(id));
    }

    @Test
    public void setPlayStatusWithGameParameter() {



        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);

        Game g = new Game();
        g.setId(id);

        insertOneGame(id);

        userDao.setPlayStatus(u, g, PlayStatus.PLAYED);

        assertEquals(PlayStatus.PLAYED, userDao.findById(u.getId()).getPlayStatus(id));

    }



    @Test
    public void testAvgRating() {

        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email, password, username);

        String email2 = "email2", password2 = "password2", username2 = "jorge2";
        final User u2 = userDao.create(email2, password2, username2);

        insertOneGame(id);
        userDao.scoreGame(u,id,10);
        userDao.scoreGame(u2,id,1);

        double avgScore = jdbcTemplate.queryForObject("SELECT avg_score FROM power_up.games WHERE id = ?", new Object[] {id}, Double.class);


        assertEquals(5.5, avgScore);

    }
}
