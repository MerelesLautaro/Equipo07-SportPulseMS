package com.Equipo07_SportPulseMS.ms_leagues.client.dto;

import java.util.List;

public record ApiSportsLeagueItem(
        League league,
        Country country,
        List<Season> seasons
) {
    public record League(
            Integer id,
            String name,
            String type,
            String logo
    ) {
    }

    public record Country(
            String name
    ) {
    }

    public record Season(
            Integer year,
            String start,
            String end,
            Boolean current
    ) {
    }
}
