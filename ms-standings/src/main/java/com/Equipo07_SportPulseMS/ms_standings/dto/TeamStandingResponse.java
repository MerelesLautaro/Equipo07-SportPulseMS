package com.Equipo07_SportPulseMS.ms_standings.dto;

public record TeamStandingResponse(
        TeamSummaryResponse team,
        StandingsResponse.LeagueInfo league,
        Integer season,
        Integer rank,
        Integer points,
        Integer played,
        String form,
        String description
) {
}
