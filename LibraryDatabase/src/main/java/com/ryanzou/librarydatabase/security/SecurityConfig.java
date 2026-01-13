package com.ryanzou.librarydatabase.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JdbcUserDetailsManager userDetailsManager;

    private LoggingAccessDeniedHandler accessDeniedHandler;

    @Autowired
    public void setAccessDeniedHandler(LoggingAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler=accessDeniedHandler;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Autowired
    @Lazy
    DataSource dataSource;

    @Autowired
    @Lazy
    BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .requestMatchers("/secured").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/books").permitAll()
                .requestMatchers("/","/**").permitAll()

                // to access h2 console
                .requestMatchers("/h2-console/**").permitAll()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/secured",true)
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        // disabling it only for accessing h2, otherwise it is a important
        // security feature and should not be disabled
        http.csrf().disable();
        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean(name = "userDetailsManager")
    public JdbcUserDetailsManager getUserDetailsManager() {
        UserDetails user = User.builder()
                .username("ryan")
                .password(passwordEncoder.encode("zou"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("amy")
                .password(passwordEncoder.encode("sun"))
                .roles("ADMIN","USER")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(admin);
        return users;
    }
}
