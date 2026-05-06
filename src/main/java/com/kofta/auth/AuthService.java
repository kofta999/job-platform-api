package com.kofta.auth;

import com.kofta.errors.ResourceAlreadyExists;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String password) {
        if (userDetailsManager.userExists(username)) {
            throw new ResourceAlreadyExists(User.class);
        }

        var newUser = User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .roles(AuthRoles.USER.toString())
            .build();

        userDetailsManager.createUser(newUser);
    }
}
