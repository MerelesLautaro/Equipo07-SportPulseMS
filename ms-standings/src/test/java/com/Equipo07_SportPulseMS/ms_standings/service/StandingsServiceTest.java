package com.Equipo07_SportPulseMS.ms_standings.service;

import com.Equipo07_SportPulseMS.ms_standings.client.ApiSportsStandingsClient;
import com.Equipo07_SportPulseMS.ms_standings.client.TeamsClient;
import com.Equipo07_SportPulseMS.ms_standings.client.dto.ApiSportsStandingsEnvelope;
import com.Equipo07_SportPulseMS.ms_standings.client.dto.TeamDetailResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.StandingsResponse;
import com.Equipo07_SportPulseMS.ms_standings.exception.TeamStandingNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandingsServiceTest {

    @Mock
    private ApiSportsStandingsClient standingsClient;

    @Mock
    private TeamsClient teamsClient;

    @InjectMocks
    private StandingsService standingsService;

    @Test
    void getStandingsReturnsRowsOrderedByRankAndEnrichedFromTeams() {
        when(standingsClient.getStandings(140, 2024)).thenReturn(envelopeWithTwoRowsUnordered());
        when(teamsClient.getTeam(529)).thenReturn(new TeamDetailResponse(529, "FC Barcelona", "barca-logo"));
        when(teamsClient.getTeam(541)).thenReturn(new TeamDetailResponse(541, "Real Madrid", "madrid-logo"));

        StandingsResponse result = standingsService.getStandings(140, 2024);

        assertEquals(2, result.standings().size());
        assertEquals(1, result.standings().get(0).rank());
        assertEquals("FC Barcelona", result.standings().get(0).team().name());
        assertEquals("barca-logo", result.standings().get(0).team().logo());
        assertEquals(2, result.standings().get(1).rank());
        assertEquals("Real Madrid", result.standings().get(1).team().name());
    }

    @Test
    void getStandingsFallsBackToApiSportsTeamDataWhenTeamsServiceFails() {
        when(standingsClient.getStandings(140, 2024)).thenReturn(envelopeWithOneRow());
        when(teamsClient.getTeam(529)).thenThrow(new RuntimeException("ms-teams down"));

        StandingsResponse result = standingsService.getStandings(140, 2024);

        assertEquals(1, result.standings().size());
        assertEquals("FC Barcelona", result.standings().get(0).team().name());
        assertEquals("api-logo", result.standings().get(0).team().logo());
    }

    @Test
    void getTeamStandingReturnsOnlyRequestedTeamPosition() {
        when(standingsClient.getStandings(140, 2024)).thenReturn(envelopeWithTwoRowsUnordered());
        when(teamsClient.getTeam(529)).thenReturn(new TeamDetailResponse(529, "FC Barcelona", "barca-logo"));
        when(teamsClient.getTeam(541)).thenReturn(new TeamDetailResponse(541, "Real Madrid", "madrid-logo"));

        var result = standingsService.getTeamStanding(529, 140, 2024);

        assertEquals(529, result.team().id());
        assertEquals("FC Barcelona", result.team().name());
        assertEquals("barca-logo", result.team().logo());
        assertEquals(1, result.rank());
        assertEquals(48, result.points());
        assertEquals(20, result.played());
        assertEquals("WWWDW", result.form());
        assertEquals("Promotion - Champions League (Group Stage)", result.description());
    }

    @Test
    void getTeamStandingThrowsNotFoundWhenTeamIsNotInStandings() {
        when(standingsClient.getStandings(140, 2024)).thenReturn(envelopeWithOneRow());
        when(teamsClient.getTeam(529)).thenReturn(new TeamDetailResponse(529, "FC Barcelona", "barca-logo"));

        assertThrows(TeamStandingNotFoundException.class, () -> standingsService.getTeamStanding(541, 140, 2024));
    }

    @Test
    void getTeamStandingFallsBackToApiSportsTeamWhenTeamsServiceFails() {
        when(standingsClient.getStandings(140, 2024)).thenReturn(envelopeWithOneRow());
        when(teamsClient.getTeam(529)).thenThrow(new RuntimeException("ms-teams down"));

        var result = standingsService.getTeamStanding(529, 140, 2024);

        assertEquals(529, result.team().id());
        assertEquals("FC Barcelona", result.team().name());
        assertEquals("api-logo", result.team().logo());
        assertEquals(1, result.rank());
        assertEquals(48, result.points());
    }

    private ApiSportsStandingsEnvelope envelopeWithTwoRowsUnordered() {
        ApiSportsStandingsEnvelope.ApiSportsStandingRow first = new ApiSportsStandingsEnvelope.ApiSportsStandingRow(
                2,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Team(541, "Real Madrid", "api-logo-rm"),
                44,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.All(
                        20,
                        13,
                        5,
                        2,
                        new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Goals(44, 20)
                ),
                24,
                "WDWWW",
                "Promotion - Champions League (Group Stage)"
        );

        ApiSportsStandingsEnvelope.ApiSportsStandingRow second = new ApiSportsStandingsEnvelope.ApiSportsStandingRow(
                1,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Team(529, "Barcelona API", "api-logo-fcb"),
                48,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.All(
                        20,
                        15,
                        3,
                        2,
                        new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Goals(52, 18)
                ),
                34,
                "WWWDW",
                "Promotion - Champions League (Group Stage)"
        );

        return new ApiSportsStandingsEnvelope(
                List.of(
                        new ApiSportsStandingsEnvelope.ApiSportsStandingLeagueResponse(
                                new ApiSportsStandingsEnvelope.ApiSportsStandingLeague(
                                        140,
                                        "La Liga",
                                        "Spain",
                                        2024,
                                        List.of(List.of(first, second))
                                )
                        )
                )
        );
    }

    private ApiSportsStandingsEnvelope envelopeWithOneRow() {
        ApiSportsStandingsEnvelope.ApiSportsStandingRow row = new ApiSportsStandingsEnvelope.ApiSportsStandingRow(
                1,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Team(529, "FC Barcelona", "api-logo"),
                48,
                new ApiSportsStandingsEnvelope.ApiSportsStandingRow.All(
                        20,
                        15,
                        3,
                        2,
                        new ApiSportsStandingsEnvelope.ApiSportsStandingRow.Goals(52, 18)
                ),
                34,
                "WWWDW",
                "Promotion - Champions League (Group Stage)"
        );

        return new ApiSportsStandingsEnvelope(
                List.of(
                        new ApiSportsStandingsEnvelope.ApiSportsStandingLeagueResponse(
                                new ApiSportsStandingsEnvelope.ApiSportsStandingLeague(
                                        140,
                                        "La Liga",
                                        "Spain",
                                        2024,
                                        List.of(List.of(row))
                                )
                        )
                )
        );
    }
}
