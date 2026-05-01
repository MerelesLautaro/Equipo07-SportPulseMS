package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team;

public record MatchDTO(

        Integer id,
        String date,

        TeamDTO homeTeam,
        TeamDTO awayTeam,

        String status

) {}