package com.Equipo07_SportPulseMS.ms_gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        return chain.filter(exchange)
                .onErrorResume(ex -> {

                    Map<String, Object> body = new HashMap<>();
                    body.put("error", "SERVICE_UNAVAILABLE");
                    body.put("message", "El servicio no está disponible");
                    body.put("timestamp", Instant.now());

                    byte[] bytes;
                    try {
                        bytes = objectMapper.writeValueAsBytes(body);
                    } catch (Exception e) {
                        bytes = new byte[0];
                    }

                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    return exchange.getResponse()
                            .writeWith(Mono.just(exchange.getResponse()
                                    .bufferFactory()
                                    .wrap(bytes)));
                });
    }

    @Override
    public int getOrder() {
        return -2; // high priority
    }
}
