package com.Equipo07_SportPulseMS.ms_dashboard.service;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard.DashboardResponse;
import com.Equipo07_SportPulseMS.ms_dashboard.service.client.*;
import com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard.impl.DashboardServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class DashboardServiceTest {

    private final FixturesClient fixturesClient = Mockito.mock(FixturesClient.class);
    private final StandingsClient standingsClient = Mockito.mock(StandingsClient.class);
    private final LeagueClient leagueClient = Mockito.mock(LeagueClient.class);
    private final ApiFootballClient apiFootballClient = Mockito.mock(ApiFootballClient.class);

    private final DashboardServiceImpl service =
            new DashboardServiceImpl(fixturesClient, standingsClient, apiFootballClient, leagueClient);


    @Test
    void shouldBuildDashboardSuccessfully() {

        mockBase();

        Mockito.when(fixturesClient.getFixtures(Mockito.any(), anyString()))
                .thenReturn(Mono.just(List.of()));

        DashboardResponse response = service.getDashboard(243, 2024).block();

        assertNotNull(response);
        assertEquals(243, response.league().id());
        assertNotNull(response.matchesToday());
        assertNotNull(response.standingsPreview());
        assertNotNull(response.topScorers());
        assertTrue(response.failedServices().isEmpty());
    }

    @Test
    void shouldUseFallbackFixturesWhenDateReturnsEmpty() {

        mockBase();

        Mockito.when(fixturesClient.getFixtures(Mockito.any(), anyString()))
                .thenReturn(Mono.just(List.of()));

        Mockito.when(fixturesClient.getFixtures(Mockito.anyInt(), Mockito.isNull()))
                .thenReturn(Mono.just(List.of()));

        DashboardResponse response = service.getDashboard(243, 2024).block();

        assertNotNull(response);
        assertEquals(243, response.league().id());
        assertNotNull(response.matchesToday());
        assertTrue(response.failedServices().isEmpty());
    }

    @Test
    void shouldHandleStandingsFailure() {

        mockBase();

        Mockito.when(fixturesClient.getFixtures(Mockito.any(), anyString()))
                .thenReturn(Mono.just(List.of()));

        Mockito.when(standingsClient.getStandings(anyInt(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("fail")));

        DashboardResponse response = service.getDashboard(243, 2024).block();

        assertNotNull(response);
        assertTrue(response.failedServices().contains("standings"));
    }

    private void mockBase() {

        Mockito.when(leagueClient.getLeagueById(anyInt()))
                .thenReturn(Mono.just(
                        new com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueDetailResponseDTO(
                                243, "Liga Pro", "Ecuador"
                        )
                ));

        Mockito.when(apiFootballClient.getTopScorers(anyInt(), anyInt()))
                .thenReturn(Mono.just(
                        new com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore.ApiTopScorerResponse(
                                List.of()
                        )
                ));

        Mockito.when(standingsClient.getStandings(anyInt(), anyInt()))
                .thenReturn(Mono.just(
                        new com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingsResponseDTO(
                                null, List.of()
                        )
                ));
    }
}