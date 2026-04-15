package com.Equipo07_SportPulseMS.ms_leagues.service;

import com.Equipo07_SportPulseMS.ms_leagues.client.ApiSportsLeaguesClient;
import com.Equipo07_SportPulseMS.ms_leagues.client.dto.ApiSportsLeagueItem;
import com.Equipo07_SportPulseMS.ms_leagues.client.dto.ApiSportsLeaguesEnvelope;
import com.Equipo07_SportPulseMS.ms_leagues.dto.LeagueSummaryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

    @Mock
    private ApiSportsLeaguesClient apiSportsLeaguesClient;

    @InjectMocks
    private LeagueService leagueService;

    @Test
    void getLeaguesWithCountryPassesFilterAndMapsResponse() {
        ApiSportsLeagueItem item = new ApiSportsLeagueItem(
                new ApiSportsLeagueItem.League(140, "La Liga", "League", "logo-url"),
                new ApiSportsLeagueItem.Country("Spain"),
                List.of(
                        new ApiSportsLeagueItem.Season(2023, "2023-08-11", "2024-05-26", false),
                        new ApiSportsLeagueItem.Season(2024, "2024-08-17", "2025-05-18", true)
                )
        );
        when(apiSportsLeaguesClient.getLeagues(null, "Spain", null))
                .thenReturn(new ApiSportsLeaguesEnvelope(List.of(item)));

        List<LeagueSummaryResponse> result = leagueService.getLeagues("Spain", null);

        verify(apiSportsLeaguesClient).getLeagues(null, "Spain", null);
        assertEquals(1, result.size());
        assertEquals(140, result.get(0).id());
        assertEquals("Spain", result.get(0).country());
        assertEquals(2024, result.get(0).currentSeason());
        assertEquals("2024-08-17", result.get(0).startDate());
        assertEquals("2025-05-18", result.get(0).endDate());
    }

    @Test
    void getLeaguesWithSeasonPassesFilterAndMapsResponse() {
        ApiSportsLeagueItem item = new ApiSportsLeagueItem(
                new ApiSportsLeagueItem.League(39, "Premier League", "League", "logo-url"),
                new ApiSportsLeagueItem.Country("England"),
                List.of(new ApiSportsLeagueItem.Season(2024, "2024-08-16", "2025-05-25", true))
        );
        when(apiSportsLeaguesClient.getLeagues(null, null, 2024))
                .thenReturn(new ApiSportsLeaguesEnvelope(List.of(item)));

        List<LeagueSummaryResponse> result = leagueService.getLeagues(null, 2024);

        verify(apiSportsLeaguesClient).getLeagues(null, null, 2024);
        assertEquals(1, result.size());
        assertEquals(39, result.get(0).id());
        assertEquals("Premier League", result.get(0).name());
        assertEquals(2024, result.get(0).currentSeason());
    }

    @Test
    void getLeaguesWithoutFiltersRequestsAllLeagues() {
        when(apiSportsLeaguesClient.getLeagues(null, null, null))
                .thenReturn(new ApiSportsLeaguesEnvelope(List.of()));

        List<LeagueSummaryResponse> result = leagueService.getLeagues(null, null);

        verify(apiSportsLeaguesClient).getLeagues(null, null, null);
        assertEquals(0, result.size());
    }
}
