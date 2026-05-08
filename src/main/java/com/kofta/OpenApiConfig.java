package com.kofta;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Job Platform API", version = "v1"),
    tags = {
        @Tag(name = "Auth"),
        @Tag(name = "Companies"),
        @Tag(name = "Software Engineers"),
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@Schema(name = "ProblemDetail", implementation = ProblemDetail.class)
public class OpenApiConfig {}
