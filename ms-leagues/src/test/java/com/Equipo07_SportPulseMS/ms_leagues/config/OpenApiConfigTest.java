package com.Equipo07_SportPulseMS.ms_leagues.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OpenApiConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void shouldExposeBearerAuthSecurityScheme() {
        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());

        SecurityScheme bearerAuth = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(bearerAuth);
        assertEquals(SecurityScheme.Type.HTTP, bearerAuth.getType());
        assertEquals("bearer", bearerAuth.getScheme());
        assertEquals("JWT", bearerAuth.getBearerFormat());
    }
}
