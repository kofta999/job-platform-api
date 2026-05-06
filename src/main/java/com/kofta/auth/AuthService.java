package com.kofta.auth;

import com.kofta.companies.Company;
import com.kofta.companies.CompanyService;
import com.kofta.errors.ResourceAlreadyExists;
import com.kofta.softwareengineers.SoftwareEngineer;
import com.kofta.softwareengineers.SoftwareEngineerService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final SoftwareEngineerService softwareEngineerService;
    private final CompanyService companyService;
    private final UserRepository userRepository;

    @Transactional
    public void registerEngineer(
        String email,
        String password,
        SoftwareEngineer engineer,
        Set<Integer> skillIds
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExists(User.class);
        }

        var user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_ENGINEER);
        // TODO: Should be disabled by default until email verification or smth
        user.setEnabled(true);

        var savedUser = userRepository.save(user);

        engineer.setUser(savedUser);

        softwareEngineerService.insertSoftwareEngineer(engineer, skillIds);
    }

    @Transactional
    public void registerCompany(
        String email,
        String password,
        Company company
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExists(User.class);
        }

        var user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_COMPANY);
        // TODO: Should be disabled by default until email verification or smth
        user.setEnabled(true);
        user.setCompany(company);

        userRepository.save(user);

        companyService.insertCompany(company);
    }
}
