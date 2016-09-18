package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static ar.edu.itba.paw.webapp.model.Filter.*;

/**
 * Data Access Object for games. Allows to search for games with specified criteria defined in {@link Filter}.
 */
@Repository
public class GameJdbcDao implements GameDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GameJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Game> searchGames(String name, Collection<Filter> filters) {
        StringBuilder nameLike = new StringBuilder("%");
        name.replace(' ', '%');
        nameLike.append(name).append('%');
        ArrayList<Game> gameList = new ArrayList<>();
        System.out.println(filters.size());
        Object[] parameters = new Object[filters.size() + 1];
        parameters[0] = nameLike.toString();
        int i = 1;
        StringBuilder searchString = new StringBuilder("SELECT power_up.games.name, power_up.games.id, avg_score, summary, power_up.platforms.name AS platformName, release FROM power_up.games, power_up.game_platforms, power_up.platforms");
        StringBuilder whereSentence = new StringBuilder(" WHERE LOWER(power_up.games.name) LIKE LOWER(?) AND power_up.game_platforms.game_id = power_up.games.id AND " +
                "power_up.game_platforms.platform_id = power_up.platforms.id");

        //Join with specific table if a filter of that table is needed
        for (Filter filter : filters) {
            //Join on relationship table
            searchString.append(", power_up.game_").append(filter.getType().name()).append(" AS  game_").append(filter.getType().name()).append("_").append(filter.getName());
            whereSentence.append(" AND power_up.games.id = game_").append(filter.getType().name()).append("_").append(filter.getName()).append(".game_id");
            //Join on entity table;
            if ((filter.getType() != FilterCategory.DEVELOPERS) && (filter.getType() != FilterCategory.PUBLISHERS)) {
                searchString.append(", power_up.").append(filter.getType().name()).append(" AS ").append(filter.getType().name()).append("_").append(filter.getName());
                whereSentence.append(" AND game_").append(filter.getType().name()).append("_").append(filter.getName()).append(".").append(filter.getType().name().substring(0, filter.getType().name().length() - 1)).append("_id = ").append(filter.getType().name()).append("_").append(filter.getName()).append(".id");
                whereSentence.append(" AND LOWER(").append(filter.getType().name()).append("_").append(filter.getName()).append(".name) = LOWER(?)");
                parameters[i] = filter.getName();
                i++;
            } else {
                searchString.append(", power_up.companies AS companies_").append(filter.getType()).append("_").append(filter.getName());
                whereSentence.append(" AND game_").append(filter.getType()).append("_").append(filter.getName()).append(".").append(filter.getType().name().substring(0, filter.getType().name().length() - 1)).append("_id = companies_").append(filter.getType().name()).append("_").append(filter.getName()).append(".id");
                whereSentence.append(" AND LOWER(companies_").append(filter.getType()).append("_").append(filter.getName()).append(".name) = LOWER(?)");
                parameters[i] = filter.getName();
                i++;
            }


        }
        searchString.append(whereSentence).append(" ORDER BY power_up.games.name").append(" LIMIT 50");
        System.out.println(searchString.toString().toLowerCase());
        jdbcTemplate.query(searchString.toString().toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Game newGame = new Game();
                        newGame.setName(rs.getString("name"));
                        newGame.setSummary(rs.getString("summary"));
                        newGame.setId(rs.getInt("id"));
                        newGame.setRelease(new LocalDate(rs.getString("release")));

                        if (!gameList.contains(newGame)) {
                            gameList.add(newGame);
                        }
                        for (Game listedGame : gameList) {
                            if (listedGame.getName().compareTo(newGame.getName()) == 0) {
                                listedGame.addPlatform(rs.getString("platformname"));
                            }
                        }

                    }
                }
        );

        return gameList;
    }

    public Game findById(int id) {
        Game result = new Game();
        Object[] parameters = new Object[1];
        parameters[0] = id;
        String query;
        query = "SELECT power_up.games.name, summary, release, avg_score FROM power_up.games WHERE power_up.games.id = ?";
        final boolean[] found = {false};
        jdbcTemplate.query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.setName(rs.getString("name"));
                        result.setSummary(rs.getString("summary"));
                        result.setAvg_score(rs.getDouble("avg_score"));
                        result.setRelease(new LocalDate(rs.getString("release")));
                        found[0] = true;
                    }
                }
        );
        if(!found[0]) {
            return null;
        }

        query = "SELECT power_up.platforms.name FROM power_up.games, power_up.platforms, power_up.game_platforms " +
                "WHERE power_up.games.id = ? AND power_up.game_platforms.game_Id = power_up.games.id AND power_up.game_platforms.platform_Id = power_up.platforms.id ";
        jdbcTemplate.query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.addPlatform(rs.getString("name"));

                    }
                }
        );
        query = "SELECT power_up.genres.name FROM power_up.games, power_up.genres, power_up.game_genres " +
                "WHERE power_up.games.id = ? AND power_up.game_genres.game_Id = power_up.games.id AND power_up.game_genres.genre_Id = power_up.genres.id ";
        jdbcTemplate.query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.addGenre(rs.getString("name"));
                    }
                }
        );
        query = "SELECT power_up.companies.name FROM power_up.games, power_up.companies, power_up.game_publishers " +
                "WHERE power_up.games.id = ? AND power_up.game_publishers.game_Id = power_up.games.id AND power_up.game_publishers.publisher_Id = power_up.companies.id ";
        jdbcTemplate.query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.addPublisher(rs.getString("name"));
                    }
                }
        );
        query = "SELECT power_up.companies.name FROM power_up.games, power_up.companies, power_up.game_developers " +
                "WHERE power_up.games.id = ? AND power_up.game_developers.game_Id = power_up.games.id AND power_up.game_developers.developer_Id = power_up.companies.id ";
        jdbcTemplate.query(query.toLowerCase(), parameters, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.addDeveloper(rs.getString("name"));
                    }
                }
        );
        return result;
    }

    Collection<String> getFiltersByType(FilterCategory filterType){
        HashSet<String> result = new HashSet<>();
        StringBuilder query = new StringBuilder().append("SELECT power_up." );
        StringBuilder fromSentence = new StringBuilder().append(" FROM power_up.");
        if(filterType!= FilterCategory.DEVELOPERS && filterType!=FilterCategory.PUBLISHERS){
            query.append(filterType.name());
            fromSentence.append(filterType.name());
        }else{
            query.append("companies");
            fromSentence.append("companies");
        }
        query.append(".name").append(fromSentence).append(" LIMIT 50");

        jdbcTemplate.query(query.toString().toLowerCase(), (Object[]) null, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.add(rs.getString(rs.getString("name")));
                    }
                }
        );

        return result;
    }
}
