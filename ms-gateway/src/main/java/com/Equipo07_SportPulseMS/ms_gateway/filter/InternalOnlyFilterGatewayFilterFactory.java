package com.Equipo07_SportPulseMS.ms_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class InternalOnlyFilterGatewayFilterFactory
        extends AbstractGatewayFilterFactory<InternalOnlyFilterGatewayFilterFactory.Config> {

    public InternalOnlyFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String internalHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Internal-Request");

            if (!"true".equals(internalHeader)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
