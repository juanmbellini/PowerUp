package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.User;
import org.junit.AfterClass;
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

import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserJdbcDaoTest {

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.execute("INSERT INTO power_up.users (id, username, email, password) VALUES (" + id + ", '" + username + "', '" + email + "', '" + password + "' );");

        Assert.assertNotNull("Created user not found by email", userDao.findByEmail(email));
        Assert.assertNotNull("Created user not found by username", userDao.findByUsername(username));

        final User u = userDao.findById(id);

        assertEquals(u.getUsername(),username);
        assertEquals(u.getEmail(),email);
        assertEquals(u.getId(),id);
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
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
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
    public void scoreNullGame(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);

        Assert.assertNotNull(u);
        userDao.scoreGame(u,null,5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreForNullUser(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");

        userDao.scoreGame(null,1,4);
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    public void scoreForNonExistingUser(){
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        userDao.scoreGame(1,1,4);
    }
    */

    @Test(expected = IllegalArgumentException.class)
    public void scoreOutOfBoundsNegative(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        userDao.scoreGame(u,1,-1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreOutOfBoundsPositive(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        userDao.scoreGame(u,1,11);

    }
    @Test
    public void scoreBound(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        userDao.scoreGame(u,1,0);
        userDao.scoreGame(u,1,10);
    }

    @Test
    public void scoreMultipleTime(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");

        userDao.scoreGame(u,1,2);

        assertEquals(2,userDao.findById(id).getScore(1));

        userDao.scoreGame(u,1,5);

        userDao.scoreGame(u,1,9);

        assertEquals(9,userDao.findById(id).getScore(1));

        userDao.scoreGame(u,1,3);


        userDao.scoreGame(u,1,4);

        assertEquals(4,userDao.findById(id).getScore(1));
    }

    @Test
    public void scoreWithGameParameter(){
        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");

        Game g = new Game();
        g.setId(id);


        userDao.scoreGame(u,g,2);

        assertEquals(2,userDao.findById(id).getScore(1));


        userDao.scoreGame(u,g,5);

        userDao.scoreGame(u,g,9);

        assertEquals(9,userDao.findById(id).getScore(1));


        userDao.scoreGame(u,g,3);


        userDao.scoreGame(u,g,4);

        assertEquals(4,userDao.findById(id).getScore(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScoreForNonScoredGame(){

        String email = "email", password = "password", username = "jorge";
        int id = 1;
        final User u = userDao.create(email,password,username);
        jdbcTemplate.execute("INSERT INTO power_up.games VALUES (1, 'Mario', 'needs: Nintendo, Nintendo 64, Platformer', 0, '2018-12-30');");
        u.getScore(1);

    }


    //TODO test playStatus


}
