package com.Equipo07_SportPulseMS.ms_dashboard.config;

import com.Equipo07_SportPulseMS.ms_dashboard.security.WebClientAuthFilter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${FIXTURES_SERVICE_URL}")
    private String fixturesBaseUrl;

    @Value("${STANDINGS_SERVICE_URL}")
    private String standingsBaseUrl;

    @Value("${LEAGUES_SERVICE_URL}")
    private String leaguesBaseUrl;

    @Value("${APISPORTS_KEY}")
    private String apiKey;

    @Value("${WEBCLIENT_TIMEOUT:3000}")
    private int timeoutMillis;

    private final WebClientAuthFilter webClientAuthFilter;

    private static final String API_FOOTBALL_BASE_URL = "https://v3.football.api-sports.io";

    private WebClient buildWebClient(String baseUrl, boolean withAuth) {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
                .responseTimeout(Duration.ofMillis(timeoutMillis))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis / 1000))
                );

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        if (withAuth) {
            builder.filter(webClientAuthFilter.bearerAuthPropagationFilter());
        }

        return builder.build();
    }

    @Bean(name = "fixturesWebClient")
    public WebClient fixturesWebClient() {
        return buildWebClient(fixturesBaseUrl, true);
    }

    @Bean(name = "standingsWebClient")
    public WebClient standingsWebClient() {
        return buildWebClient(standingsBaseUrl, true);
    }

    @Bean(name = "leaguesWebClient")
    public WebClient leaguesWebClient() {
        return buildWebClient(leaguesBaseUrl, true);
    }

    @Bean(name = "apiFootballWebClient")
    public WebClient apiFootballWebClient() {
        return buildWebClient(API_FOOTBALL_BASE_URL, false)
                .mutate()
                .defaultHeader("x-apisports-key", apiKey)
                .build();
    }
}