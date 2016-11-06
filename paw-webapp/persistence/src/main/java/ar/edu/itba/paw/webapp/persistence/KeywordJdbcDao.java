//package ar.edu.itba.paw.webapp.persistence;
//
//import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
//import ar.edu.itba.paw.webapp.interfaces.KeywordDao;
//import ar.edu.itba.paw.webapp.model.Game;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.RowCallbackHandler;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by Juan Marcos Bellini on 19/10/16.
// * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
// *
// * Implementation of {@link KeywordDao} oriented towards JDBC.
// */
//@Repository
//public class KeywordJdbcDao extends BaseJdbcDao implements KeywordDao {
//
//    @Autowired
//    public KeywordJdbcDao(DataSource dataSource) {
//        super(dataSource);
//    }
//
//    @Override
//    public Set<String> getGameKeywords(Game game) {
//        return getGameKeywords(game.getId());
//    }
//
//    @Override
//    public Set<String> getGameKeywords(long gameId) {
//
//        Set<String> result = new HashSet<>();
//        Object[] parameters = {gameId};
//        StringBuilder query = new StringBuilder("SELECT keywords.name FROM games")
//                .append(" INNER JOIN game_keywords ON games.id = game_keywords.game_id")
//                .append(" INNER JOIN keywords ON game_keywords.keyword_id = keywords.id")
//                .append(" WHERE games.id = ?");
//        try {
//            getJdbcTemplate().query(query.toString().toLowerCase(), parameters, new RowCallbackHandler() {
//                        @Override
//                        public void processRow(ResultSet rs) throws SQLException {
//                            result.add(rs.getString("name"));
//                        }
//                    }
//            );
//        } catch (Exception e) {
//            throw new FailedToProcessQueryException();
//        }
//        return result;
//    }
//}
