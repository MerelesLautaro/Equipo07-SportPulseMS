package com.Equipo07_SportPulseMS.ms_leagues.dto;

public record LeagueSummaryResponse(
        Integer id,
        String name,
        String type,
        String country,
        String logo,
        Integer currentSeason,
        String startDate,
        String endDate
) {
}
