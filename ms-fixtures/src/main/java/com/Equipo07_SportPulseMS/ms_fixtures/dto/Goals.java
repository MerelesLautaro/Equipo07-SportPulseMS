package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Goals(
        Integer home,
        Integer away
) {
}
