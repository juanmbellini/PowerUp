package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.json.JsonAuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.auth.json.JsonAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.json.JsonAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationProvider;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.config"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationProvider jwtAuthProvider;

    @Autowired
    private JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;

    @Autowired
    private JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(protectedEndpointsMatcher());
        filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public RequestMatcher protectedEndpointsMatcher() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/**", "POST"),
            new AntPathRequestMatcher("/**", "PUT"),
            new AntPathRequestMatcher("/**", "DELETE"),
            new AntPathRequestMatcher("/**", "PATCH"),
            optionallyAuthenticatedEndpointsMatcher()
        );
    }

    /**
     * Some endpoints may optionally accept authentication, and possibly return different responses in that case. The
     * endpoints matched by this matcher MUST also work without authentication, although the response may be different
     * than if authenticated.
     *
     * @return A matcher for optionally authenticated endpoints.
     */
    @Bean
    public RequestMatcher optionallyAuthenticatedEndpointsMatcher() {
        return new OrRequestMatcher(
            // TODO hacer la lista más específica para no procesar endpoints de más
            new AntPathRequestMatcher("/**", "GET")
        );
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login","POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler);
        return filter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
            .addFilterBefore(jsonAuthFilter(), UsernamePasswordAuthenticationFilter.class) //Use JSON login for initial authentication
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)  //Protect all other necessary endpoints with JWT
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().logout().disable()
            .rememberMe().disable()
            .csrf().disable();
    }

    @Autowired
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthProvider)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico", "/403")
            .antMatchers(HttpMethod.POST, "/api/users");
    }
}
