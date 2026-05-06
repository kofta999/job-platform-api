package com.kofta;

import com.kofta.auth.AuthRoles;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public UserDetailsManager userDetailsManager(
        DataSource dataSource,
        PasswordEncoder encoder
    ) {
        UserDetails kofta = User.builder()
            .username("kofta")
            .password(encoder.encode("kofta"))
            .roles(AuthRoles.USER.toString())
            .build();

        UserDetails adham = User.builder()
            .username("adham")
            .password(encoder.encode("adham"))
            .roles(AuthRoles.USER.toString(), AuthRoles.ADMIN.toString())
            .build();

        var manager = new JdbcUserDetailsManager(dataSource);
        if (!manager.userExists("kofta")) {
            manager.createUser(kofta);
        }

        if (!manager.userExists("adham")) {
            manager.createUser(adham);
        }

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
