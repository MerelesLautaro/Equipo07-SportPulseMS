package com.Equipo07_SportPulseMS.ms_standings.controller;

import com.Equipo07_SportPulseMS.ms_standings.dto.StandingsResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.TeamStandingResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.TeamSummaryResponse;
import com.Equipo07_SportPulseMS.ms_standings.service.StandingsService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StandingsControllerTest {

    private final StandingsService standingsService = mock(StandingsService.class);
    private final StandingsController controller = new StandingsController(standingsService);

    @Test
    void getTeamStandingDelegatesToServiceAndReturnsResponse() {
        TeamStandingResponse expected = new TeamStandingResponse(
                new TeamSummaryResponse(529, "FC Barcelona", "barca-logo"),
                new StandingsResponse.LeagueInfo(140, "La Liga", "Spain", 2024),
                2024,
                1,
                48,
                20,
                "WWWDW",
                "Promotion - Champions League (Group Stage)"
        );
        when(standingsService.getTeamStanding(529, 140, 2024)).thenReturn(expected);

        TeamStandingResponse result = controller.getTeamStanding(529, 140, 2024);

        assertEquals(expected, result);
    }
}
