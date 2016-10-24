package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
import ar.edu.itba.paw.webapp.interfaces.PictureDao;
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
 */
@Repository
public class PictureJdbcDao extends BaseJdbcDao implements PictureDao {

    @Autowired
    public PictureJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Set<String> getGamePictures(Game game) {
        return getGamePictures(game.getId());
    }

    @Override
    public Set<String> getGamePictures(long gameId) {

        Set<String> result = new HashSet<>();
        Object[] parameters = {gameId};
        String query = "SELECT cloudinary_id FROM game_pictures WHERE game_id = ? ORDER BY id ASC";
        try {
            getJdbcTemplate().query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs) throws SQLException {
                            result.add(rs.getString("cloudinary_id"));
                        }
                    }
            );
        } catch (Exception e) {
            throw new FailedToProcessQueryException();
        }
        return result;
    }
}
