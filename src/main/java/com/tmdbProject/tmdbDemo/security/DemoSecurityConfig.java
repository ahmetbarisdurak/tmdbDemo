package com.tmdbProject.tmdbDemo.security;
/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) throws SQLException {

        Logger logger = LoggerFactory.getLogger(DemoSecurityConfig.class);

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, pw, enabled FROM user WHERE email=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT email, user_role FROM roles WHERE email=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {


        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers( "/api/login").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers( "/api/users/**" ).hasAnyAuthority("ADMIN")
                );

        System.out.println("Authorization process");
/*
        http.authorizeHttpRequests(configurer ->
                configurer
                        .anyRequest().permitAll());


        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();

    }
}
*/
