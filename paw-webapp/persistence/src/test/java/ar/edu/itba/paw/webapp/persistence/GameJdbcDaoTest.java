package ar.edu.itba.paw.webapp.persistence;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.junit.Test;

import javax.sql.DataSource;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

/**
 * Created by dgrimau on 14/09/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class GameJdbcDaoTest {

    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";

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

    }


}





