package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Fixture(
        Integer id,
        String referee,
        String timezone,
        String date,
        Long timestamp,
        Periods periods,
        Venue venue,
        Status status
) {
}
