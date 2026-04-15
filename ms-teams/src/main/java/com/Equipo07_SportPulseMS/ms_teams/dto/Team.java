package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Team(
        Integer id,
        String name,
        String code,
        String country,
        Integer founded,
        boolean national,
        String logo
) {
}
