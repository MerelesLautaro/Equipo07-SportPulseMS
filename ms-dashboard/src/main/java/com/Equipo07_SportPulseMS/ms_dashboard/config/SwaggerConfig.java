package com.Equipo07_SportPulseMS.ms_dashboard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ms-dashboard API",
                version = "1.0.0",
                description = """
                        Servicio de dashboard deportivo que agrega información de múltiples microservicios:
                        fixtures, standings, leagues y top scorers.
                        
                        🔐 Requiere autenticación JWT para acceder a los endpoints.
                        
                        📊 Los datos de top scorers provienen de una API externa (API-Sports),
                        por lo que el sistema requiere una API Key interna configurada.
                        """
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT requerido para acceder al dashboard"
)
@SecurityScheme(
        name = "internalApiKey",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "x-apisports-key",
        description = "API Key necesaria para consumir datos de la API externa (Top Scorers)"
)
public class SwaggerConfig {
}