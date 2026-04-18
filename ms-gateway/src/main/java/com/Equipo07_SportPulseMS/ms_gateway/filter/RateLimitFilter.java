package com.Equipo07_SportPulseMS.ms_gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final long LIMIT = 60;
    private static final long WINDOW_SECONDS = 60;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String ip = extractIp(exchange);
        String key = buildKey(ip);

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }

        if (count != null && count > LIMIT) {
            return buildRateLimitResponse(exchange, key);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> buildRateLimitResponse(ServerWebExchange exchange, String key) {

        Long ttl = redisTemplate.getExpire(key);
        long retryAfter = ttl > 0 ? ttl : WINDOW_SECONDS;

        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().add("Retry-After", String.valueOf(retryAfter));

        Map<String, Object> body = new HashMap<>();
        body.put("error", "RATE_LIMIT_EXCEEDED");
        body.put("message", "Demasiadas peticiones. Límite: 10 req/min");
        body.put("retryAfter", retryAfter);
        body.put("timestamp", Instant.now().toString());

        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(body);

            DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);

            return response.writeWith(Mono.just(buffer))
                    .then(Mono.empty());

        } catch (Exception e) {
            return response.setComplete();
        }
    }

    private String extractIp(ServerWebExchange exchange) {
        String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");

        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }

        if (exchange.getRequest().getRemoteAddress() != null) {
            return exchange.getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }

        return "unknown";
    }

    private String buildKey(String ip) {
        long window = Instant.now().getEpochSecond() / 60;
        return "rate_limit:" + ip + ":" + window;
    }

    @Override
    public int getOrder() {
        return -10;
    }
}