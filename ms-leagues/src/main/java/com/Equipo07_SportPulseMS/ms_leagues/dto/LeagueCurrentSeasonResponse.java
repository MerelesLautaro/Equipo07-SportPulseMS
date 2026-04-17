package com.Equipo07_SportPulseMS.ms_leagues.dto;

public record LeagueCurrentSeasonResponse(
        Integer year,
        String startDate,
        String endDate,
        Boolean current
) {
}
