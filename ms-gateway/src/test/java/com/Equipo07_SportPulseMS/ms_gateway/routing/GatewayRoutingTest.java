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
class GatewayRoutingTest {

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

        registry.add("services.auth", authServer::getBaseUrl);
        registry.add("services.leagues", () -> "http://localhost:9992");
        registry.add("services.teams", () -> "http://localhost:9993");
        registry.add("services.fixtures", () -> "http://localhost:9994");
        registry.add("services.standings", () -> "http://localhost:9995");
        registry.add("services.notifications", () -> "http://localhost:9996");
        registry.add("services.dashboard", () -> "http://localhost:9997");
    }

    @Test
    void shouldRouteToAuthService() {

        authServer.enqueueResponse("{\"token\":\"abc123\"}");

        var result = webTestClient.post()
                .uri("/api/auth/login")
                .header("Content-Type", "application/json")
                .bodyValue("{\"username\":\"test\",\"password\":\"123\"}")
                .exchange()
                .expectBody(String.class)
                .returnResult();

        System.out.println("STATUS: " + result.getStatus());
        System.out.println("BODY: " + result.getResponseBody());

        Assertions.assertEquals(200, result.getStatus().value());
    }
}