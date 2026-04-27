package com.Equipo07_SportPulseMS.ms_standings.dto;

import java.util.List;

public record StandingsResponse(
        LeagueInfo league,
        List<StandingEntryResponse> standings
) {
    public record LeagueInfo(
            Integer id,
            String name,
            String country,
            Integer season
    ) {
    }
}
