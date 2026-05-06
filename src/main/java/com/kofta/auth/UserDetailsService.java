package com.kofta.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService
{

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        var user = userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "User not found with email: " + email
                )
            );

        Integer engId = (user.getRole() == Role.ROLE_ENGINEER &&
            user.getSoftwareEngineer() != null)
            ? user.getSoftwareEngineer().getId()
            : null;

        Integer compId = (user.getRole() == Role.ROLE_COMPANY &&
            user.getCompany() != null)
            ? user.getCompany().getId()
            : null;

        return new AuthUser(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.isEnabled(),
            user.getRole(),
            engId,
            compId
        );
    }
}
