package com.Equipo07_SportPulseMS.ms_standings.dto;

public record StandingEntryResponse(
        Integer rank,
        TeamSummaryResponse team,
        Integer points,
        Integer played,
        Integer won,
        Integer drawn,
        Integer lost,
        Integer goalsFor,
        Integer goalsAgainst,
        Integer goalDifference,
        String form
) {
}
