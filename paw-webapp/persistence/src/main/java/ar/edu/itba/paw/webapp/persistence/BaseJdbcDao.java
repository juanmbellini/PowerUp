package ar.edu.itba.paw.webapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * This class represents a BaseJdbcDao, which implements methods all JdbcDaos will need
 */
public abstract class BaseJdbcDao {


    /**
     * A Jdbc template to make database operations
     */
    private JdbcTemplate jdbcTemplate;

    public BaseJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* package */ JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
