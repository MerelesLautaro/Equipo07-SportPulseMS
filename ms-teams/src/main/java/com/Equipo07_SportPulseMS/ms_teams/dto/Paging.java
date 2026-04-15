package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Paging(
        int current,
        int total
) {
}
