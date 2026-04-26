package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamResponse(
        Integer id,
        String name,
        String country,
        String logo,
        Integer founded,
        StadiumResponse stadium
) {
}
