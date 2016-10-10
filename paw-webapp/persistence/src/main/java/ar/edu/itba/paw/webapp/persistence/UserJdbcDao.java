package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of {@link ar.edu.itba.paw.webapp.interfaces.UserDao} oriented towards JDBC.
 */
@Repository
public class UserJdbcDao implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userCreator;
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"), rs.getString("email"), rs.getString("username"));
        }
    };


    @Autowired
    public UserJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        userCreator = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("power_up")
                .withTableName("users")
                .usingColumns("username", "email", "password")
                .usingGeneratedKeyColumns("id");
    }

    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public User create(String email, String password, String username) {
        //Schema doesn't allow for UNIQUE usernames with NULLs, so we must check uniqueness manually
        jdbcTemplate.query(
                "SELECT count(*) AS ct FROM power_up.users WHERE username = ? LIMIT 1", new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if (rs.getInt("ct") >= 1) {
                            throw new UserExistsException("User with username " + email + " already exists");
                        }
                    }
                },
                username);

        final Map<String, Object> args = new HashMap<>();
        args.put("email", email);           //TODO ensure it's a valid email
        args.put("password", password);     //TODO hash or salt the passwords here, DON'T STORE THEM IN PLAIN TEXT!
        args.put("username", username);
        try {
            Number id = userCreator.executeAndReturnKey(args);
            return new User(id.longValue(), email, username);
        }
        catch (Exception e) {
            throw new UserExistsException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        final String query = "SELECT * FROM power_up.users WHERE username = ? LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, username);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public User findByEmail(String email) {
        final String query = "SELECT * FROM power_up.users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, email);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public User findById(long id) {
        final String query = "SELECT * FROM power_up.users WHERE id = ? LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, id);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean scoreGame(User user, long gameId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
