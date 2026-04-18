package com.Equipo07_SportPulseMS.ms_gateway.controller;

import com.Equipo07_SportPulseMS.ms_gateway.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = HealthController.class)
@AutoConfigureWebTestClient
class HealthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private HealthService healthService;

    @Test
    @WithMockUser
    void shouldReturnGatewayHealth() {

        when(healthService.getServicesHealth())
                .thenReturn(Mono.just(Map.of(
                        "ms-auth", "UP",
                        "ms-teams", "DOWN"
                )));

        webTestClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gateway").isEqualTo("UP")
                .jsonPath("$.services.ms-auth").isEqualTo("UP")
                .jsonPath("$.services.ms-teams").isEqualTo("DOWN");
    }
}