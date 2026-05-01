package com.Equipo07_SportPulseMS.ms_gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ms-gateway API",
                version = "1.0.0",
                description = "Gateway del sistema SportPulse. Actúa como proxy inverso y expone el estado de los microservicios."
        )
)
public class SwaggerConfig {
}