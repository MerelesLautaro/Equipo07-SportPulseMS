package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureItem(
        Fixture fixture,
        League league,
        Teams teams,
        Goals goals,
        Score score
) {
}
