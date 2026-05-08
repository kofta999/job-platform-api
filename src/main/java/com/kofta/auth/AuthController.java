package com.kofta.auth;

import com.kofta.companies.CompanyMapper;
import com.kofta.softwareengineers.SoftwareEngineerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and registration")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final SoftwareEngineerMapper engineerMapper;
    private final CompanyMapper companyMapper;

    @PostMapping("/login")
    @Operation(summary = "Login", operationId = "authLogin")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public String login(@RequestBody @Valid LoginDto credentials) {
        var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.email(),
                credentials.password()
            )
        );

        AuthUser user = (AuthUser) auth.getPrincipal();

        return jwtService.generateToken(user);
    }

    @PostMapping("/register/engineer")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Register engineer", operationId = "registerEngineer")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Already exists",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public void registerEngineer(
        @RequestBody @Valid RegisterEngineerDto credentials
    ) {
        authService.registerEngineer(
            credentials.email(),
            credentials.password(),
            engineerMapper.fromCreateDto(credentials.profile()),
            credentials.profile().skillIds()
        );
    }

    @PostMapping("/register/company")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Register company", operationId = "registerCompany")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Already exists",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public void registerCompany(
        @RequestBody @Valid RegisterCompanyDto credentials
    ) {
        authService.registerCompany(
            credentials.email(),
            credentials.password(),
            companyMapper.fromCreateDto(credentials.companyDetails())
        );
    }
}
