package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
import ar.edu.itba.paw.webapp.interfaces.PlatformDao;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Implementation of {@link PlatformDao} oriented towards JDBC.
 */
@Repository
public class PlatformJdbcDao extends BaseJdbcDao implements PlatformDao {


    @Autowired
    public PlatformJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Map<String, LocalDate> getGamePlatforms(Game game) {
        return getGamePlatforms(game.getId());
    }

    @Override
    public Map<String, LocalDate> getGamePlatforms(long gameId) {

        Map<String, LocalDate> result = new HashMap<>();
        Object[] parameters = {gameId};
        StringBuilder query = new StringBuilder("SELECT platforms.name, release_date FROM games")
                .append(" INNER JOIN game_platforms ON games.id = game_platforms.game_id")
                .append(" INNER JOIN platforms ON game_platforms.platform_id = platforms.id")
                .append(" WHERE games.id = ?");
        try {
            getJdbcTemplate().query(query.toString().toLowerCase(), parameters, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs) throws SQLException {
                            result.put(rs.getString("name"), LocalDate.parse(rs.getString("release_date")));
                        }
                    }
            );
        } catch (Exception e) {
            throw new FailedToProcessQueryException();
        }
        return result;
    }
}
