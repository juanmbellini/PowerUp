package ar.edu.itba.paw.webapp.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@ComponentScan({"ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.webapp.persistence",
        "ar.edu.itba.paw.webapp.service",
        "ar.edu.itba.paw.webapp.external",
        "ar.edu.itba.paw.webapp.config",
        "ar.edu.itba.paw.webapp.scheduled",
        "ar.edu.itba.paw.webapp.mail"})
@EnableScheduling
@PropertySources({
        @PropertySource(value = "classpath:config/common.properties", ignoreResourceNotFound = false),
        @PropertySource(value = "classpath:config/passwords.properties", ignoreResourceNotFound = false),
        @PropertySource(value = "classpath:config/production.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/development.properties", ignoreResourceNotFound = true),
        //IMPORTANT!! In case of duplicates, the last file declared here overrides the others
})
public class WebConfig {

    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        final String hostName = environment.getRequiredProperty("db.host");
        final Integer port = environment.getRequiredProperty("db.port", Integer.class);
        final String dbName = environment.getRequiredProperty("db.name");
        final String username = environment.getRequiredProperty("db.username");
        final String password = environment.getRequiredProperty("db.password");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + hostName + ":" + port + "/" + dbName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.webapp.model");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto","update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
// Si ponen esto en prod, hay tabla!!!
//                properties.setProperty("hibernate.show_sql", "true");
//                        properties.setProperty("format_sql", "true");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        return dsi;
    }

    @Bean(initMethod = "migrate")
        // Migrate DB on app start
    Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource());
        flyway.setLocations("classpath:migration");
        return flyway;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/errorMessages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(environment.getRequiredProperty("email.username"));
        mailSender.setPassword(environment.getRequiredProperty("email.password"));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");    //Trust Gmail's SSL certificate
//        javaMailProperties.put("mail.debug", "true"); //Prints out everything on screen

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
