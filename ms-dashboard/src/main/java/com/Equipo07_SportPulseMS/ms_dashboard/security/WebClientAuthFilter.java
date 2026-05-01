package com.Equipo07_SportPulseMS.ms_dashboard.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

@Component
public class WebClientAuthFilter {

    public ExchangeFilterFunction bearerAuthPropagationFilter() {
        return (request, next) ->
                ReactiveSecurityContextHolder.getContext()
                        .map(ctx -> ctx.getAuthentication())
                        .flatMap(auth -> {

                            if (auth.getCredentials() == null) {
                                return next.exchange(request);
                            }

                            String token = auth.getCredentials().toString();

                            ClientRequest newRequest = ClientRequest.from(request)
                                    .headers(headers -> headers.setBearerAuth(token))
                                    .build();

                            return next.exchange(newRequest);
                        })
                        .switchIfEmpty(next.exchange(request));
    }
}