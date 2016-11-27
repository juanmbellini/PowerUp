package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.config"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
            .sessionManagement()
                .invalidSessionUrl("/login")
            .and().authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/rateAndUpdateStatus").authenticated()
                .antMatchers("/update-shelves-by-game").authenticated()
                .antMatchers("/**-shelf").authenticated()
                .antMatchers("/write-review").authenticated()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
            .and().formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            .and().rememberMe()
                .userDetailsService(userDetailsService)
                .rememberMeParameter("rememberMe")
                .key("MIIEpQIBAAKCAQEAnRousecdD54t+HcaC+41DP0yKUayPr1lCPXnV8n1DvXZ7Hmg\n" +
                    "xFoqVJnP7t7yhJmGiIM8+iWfUGPqJKDu/Riwo429kI1U0trar8DfyFKOOTwIGhA+\n" +
                    "Pu2Okv7D2aZVTThxqu8iMB8UEZ516ZnHQIwTfmGrZhvM5U73WVQntjac7U3mFz4b\n" +
                    "gsb6zXgZc4c+pPOVGBwi0VIwVpmmLTv9y9gtmT8Mm6jBa3aWgkyCejEjT9sn57f9\n" +
                    "QGxkPVbrYSytVPXOYw8uO2oWnV6PedMtVAbqqH3n9/Bc0fdY1Kx6dQyr3Zp5uvQT\n" +
                    "ALehAbywdc003jdhf/dxw2BYujTLuyb6REilxQIDAQABAoIBAQCQ7y+3Bp1j5C1K\n" +
                    "9S39ZbRhmFEnjUYx5W6Jlrrn3bSMKbnzlL4Bh6FXzVLsb5hTRoO7+z9NE1pnwtWn\n" +
                    "FyWEL7v+F2yUKB7iK+/mhsytNaRqHvzmdqfGTEjlSc4LRI2boQAUj2r99B4Cpyrm\n" +
                    "6OzOmqv9Q0Pp/qnHv1MogR/l1Xpu3aXkLXg0sFwyuTDIH3SKFkJcPecllXzejtVs\n" +
                    "J291MFXKwZntxUk02QCKsmfnrje90sgQOEXNtAJQvGE8K6d0oGEtfo73RJ9PokSN\n" +
                    "xdFoAeiFrgUn9BwIkXTJ26PFMhDq09kkRIfk7Rap3MYEg+DgXsboWnJ8jaWjNbee\n" +
                    "co7bfqO9AoGBAMq0fkJTStfVzlkVBAaxgrVnuPCAOEEE8ltcB+UOmx4QB/M2iNfZ\n" +
                    "AWfxa4BAB1vrqXTKawekucz9uIo2lGr66gtp4XPp8f6R4ZVcjuBOpg3k2KYduQkL\n" +
                    "F5836V3v3Y2A4vXNnb/PWhxTTL4Sdm5Nn32Y9Y4y+Pf7USScVenfmKFDAoGBAMZo\n" +
                    "S2qhrm0GvTwbMFdOgEV8+bsoYQLm0a4lU/BgdgbdNdfOg/2Qq4ipPyGGis+M5h3a\n" +
                    "TXARY/IoDYI+zxntYb65CoPJnUrmZQyTMcAXwvtg93wdPLNPCqdG9+MhqVLewJoX\n" +
                    "oQ6HuYtxikVc94+eDhspWKa4NVaOATMHNv/hRUhXAoGBAJM/cCRQCyMknkPZ31XZ\n" +
                    "ZuDOGushyTt6E2/IN7ft10KMVKoZaGibq8jM99FvMalVVICRdhRUVeASQxartT7N\n" +
                    "TGzEGlEwlWjeoeb1GJjaqQeYwMRS/RITq7IuVGi3kNJ02OnD1p76SjQfUrUUBlH0\n" +
                    "MzJyhZYpcu/48SXOJx7AHUivAoGAa2y0yLtZ0bZAZ3bhKaRbV0RfgrJONF/9T6ju\n" +
                    "Vcwkm3rSWFJ8rKHT/l6EzAYoyk+jmK5GF1OTJd4B0m9nesZIkhdmVgynmZI9TB22\n" +
                    "Zid3btwFo7HA1+UIA6ItPVFQeIobBlOc5F0gXRvQndXERIJzaMluMnayinbAt3xE\n" +
                    "jy7NcGkCgYEAuDeiIbKhafQwOR24Prn0NfjVoS+78LaEQE6hto7NwVSqm481O6g4\n" +
                    "I79YbIPAIXXzGfFcXunvdca0GXMGNDnih2k711uCTBe54OUDHE/CgNpWqI9PMv/i\n" +
                    "pbBgtN6rvSachbt9xNgYt29/SF3poVrlyZH/OrbEMlX1la0UFiZYtkw=")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf()
                .disable();
    }

    @Autowired
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        //TODO delete default PAW user this in production from bottom of initial-data.sql
        auth.userDetailsService(userDetailsService)
            .and().jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, hashed_password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM user_authorities WHERE username = ?");;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico", "/403");
    }
}