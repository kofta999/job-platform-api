package com.kofta.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final AuthenticationManager authManager;

    public AuthController(
        JwtService jwtService,
        AuthService authService,
        AuthenticationManager authManager
    ) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.authManager = authManager;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDto credentials) {
        var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.username(),
                credentials.password()
            )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(user);
    }

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterDto credentials) {
        authService.createUser(credentials.username(), credentials.password());
    }
}
