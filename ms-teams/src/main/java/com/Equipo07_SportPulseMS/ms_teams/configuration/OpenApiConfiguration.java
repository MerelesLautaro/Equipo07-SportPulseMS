package com.Equipo07_SportPulseMS.ms_teams.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(this.getCustomInfo())
                .components(this.getJwtComponent());
    }

    private Info getCustomInfo() {
        return new Info()
                .title("MS Teams API")
                .version("1.0")
                .description("API para gestionar equipos de fútbol");
    }

    private Components getJwtComponent() {
        return new Components().addSecuritySchemes(
                "bearerAuth",
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
        );
    }
}
