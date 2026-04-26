package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record League(
        Integer id,
        String name,
        String country,
        String logo,
        String flag,
        Integer season,
        String round,
        Boolean standings
) {
}
