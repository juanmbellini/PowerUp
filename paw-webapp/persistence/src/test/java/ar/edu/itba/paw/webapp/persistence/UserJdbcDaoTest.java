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

    @Before
    public void cleanUp() {
        JdbcTestUtils.deleteFromTables(userDao.getJdbcTemplate(), "power_up.users");
    }

    @Test
    public void testFindWithEmptyTable(){
        assertNull("Found a user by ID when the table is empty", userDao.findById(42));
        assertNull("Found a user by email when the table is empty", userDao.findByEmail("nonexistent"));
        assertNull("Found a user by username when the table is empty", userDao.findByUsername("nonexistent"));
    }

    @Test
    public void testFindWithNonEmptyTable() {
        String email = "email", password = "password", username = "lalala";
        userDao.create(email, password, username);

        Assert.assertNotNull("Created user not found by email", userDao.findByEmail(email));
        Assert.assertNotNull("Created user not found by username", userDao.findByUsername(username));
    }

    @Test
    public void testCreateWithAllInfo() {
        String email = "email";
        String username = "user";
        String password = "password";

        User u = userDao.create(email, password, username);
        Assert.assertNotNull("Created user is null", u);
        assertEquals("Mismatching created user email", u.getEmail(), email);
        assertEquals("Mismatching created user username", u.getUsername(), username);
    }

    @Test
    public void testCreateWithoutUsername() {
        String email = "email";
        String password = "password";

        User u = userDao.create(email, password, null);
        Assert.assertNotNull("Created user is null", u);
        assertEquals("Mismatching created user email", u.getEmail(), email);
        assertEquals("Created user without username but got a different username", u.getUsername(), null);
    }

    @Test(expected = UserExistsException.class)
    public void testDuplicateEmail() {
        String email = "email";
        String username = "user";
        String password = "password";

        User u = userDao.create(email, password, username);
        User neverCreated = userDao.create(email, password, username);
    }

    @Test(expected = UserExistsException.class)
    public void testDuplicateUsername() {
        User u = userDao.create("email1", "password", "username");
        User neverCreated = userDao.create("email2", "password", "username");
    }
}
