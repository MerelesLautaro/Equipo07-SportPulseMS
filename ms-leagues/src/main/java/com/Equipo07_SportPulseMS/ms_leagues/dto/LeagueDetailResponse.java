package com.Equipo07_SportPulseMS.ms_leagues.dto;

import java.util.List;

public record LeagueDetailResponse(
        Integer id,
        String name,
        String type,
        String country,
        String logo,
        List<Integer> seasons,
        LeagueCurrentSeasonResponse currentSeason
) {
}
