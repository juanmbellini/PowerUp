package ar.edu.itba.paw.webapp.persistence;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by dgrimau on 14/09/16.
 */
@ComponentScan({"ar.edu.itba.paw.webapp.persistence",})
@Configuration
public class TestConfig {
    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(JDBCDriver.class);
        ds.setUrl("jdbc:hsqldb:mem:paw");
        ds.setUsername("ha");
        ds.setPassword("");

        //Opens a window with the in-memory database manager so we can perform queries and stuff. Closes after tests
        //are complete. To use, uncomment the lines below, set a breakpoint somewhere NOT in this file,
        // right click the breakpoint and make it suspend only its thread, not all threads. Otherwise it will also
        // suspend the manager's thread and it will be frozen.
//        org.hsqldb.util.DatabaseManager.main(new String[] {
//                "--url", "jdbc:hsqldb:mem:paw", "--noexit", "--user", "ha"//, "--password", ""
//        });

        return ds;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.webapp.model");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }


    @Bean
    public PlatformTransactionManager transactionManager(
            final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


}