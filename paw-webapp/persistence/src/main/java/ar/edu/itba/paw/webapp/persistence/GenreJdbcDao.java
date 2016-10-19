package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Implementation of {@link GenreDao} oriented towards JDBC.
 */
@Repository
public class GenreJdbcDao extends BaseJdbcDao implements GenreDao {


    @Autowired
    public GenreJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Set<String> getGameGenres(Game game) {
        return getGameGenres(game.getId());
    }

    @Override
    public Set<String> getGameGenres(long gameId) {

        Set<String> result = new HashSet<>();
        Object[] parameters = {gameId};
        StringBuilder query = new StringBuilder("SELECT genres.name FROM games")
                .append(" INNER JOIN game_genres ON games.id = game_genres.game_id")
                .append(" INNER JOIN genres ON game_genres.genre_id = genres.id")
                .append(" WHERE games.id = ?;");
        try {
            getJdbcTemplate().query(query.toString().toLowerCase(), parameters, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs) throws SQLException {
                            result.add(rs.getString("name"));
                        }
                    }
            );
        } catch (Exception e) {
            throw new FailedToProcessQueryException();
        }
        return result;
    }
}
