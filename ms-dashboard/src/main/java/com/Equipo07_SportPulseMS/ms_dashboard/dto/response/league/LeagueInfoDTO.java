package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league;

public record LeagueInfoDTO(
        Integer id,
        String name,
        String country,
        Integer season
) {}