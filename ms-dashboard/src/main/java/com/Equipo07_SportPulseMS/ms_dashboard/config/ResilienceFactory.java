package com.Equipo07_SportPulseMS.ms_dashboard.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ResilienceFactory {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    private static final String INSTANCE = "dashboardService";

    public <T> Function<Mono<T>, Mono<T>> decorate() {

        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(INSTANCE);
        Retry retry = retryRegistry.retry(INSTANCE);

        return mono -> mono
                .transformDeferred(CircuitBreakerOperator.of(cb))
                .transformDeferred(RetryOperator.of(retry));
    }
}