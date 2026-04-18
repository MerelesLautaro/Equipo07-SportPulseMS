package com.Equipo07_SportPulseMS.ms_gateway.routing;

import com.Equipo07_SportPulseMS.ms_gateway.util.MockServerManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class GatewayFallbackTest {

    @Autowired
    private WebTestClient webTestClient;

    static MockServerManager authServer = new MockServerManager();

    @BeforeAll
    static void setup() throws Exception {
        authServer.start();
    }

    @AfterAll
    static void teardown() throws Exception {
        authServer.shutdown();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("AUTH_SERVICE_URL", authServer::getBaseUrl);

        registry.add("LEAGUES_SERVICE_URL", () -> "http://localhost:9992");
        registry.add("TEAMS_SERVICE_URL", () -> "http://localhost:9993");
        registry.add("FIXTURES_SERVICE_URL", () -> "http://localhost:9994");
        registry.add("STANDINGS_SERVICE_URL", () -> "http://localhost:9995");
        registry.add("NOTIFICATIONS_SERVICE_URL", () -> "http://localhost:9996");
        registry.add("DASHBOARD_SERVICE_URL", () -> "http://localhost:9997");
    }

    @Test
    void shouldReturn503WhenServiceDown() {

        webTestClient.get()
                .uri("/api/auth/login")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody()
                .jsonPath("$.error").isEqualTo("SERVICE_UNAVAILABLE");
    }
}