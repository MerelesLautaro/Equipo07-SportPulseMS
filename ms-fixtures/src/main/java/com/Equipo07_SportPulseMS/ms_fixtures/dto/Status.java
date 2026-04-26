package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Status(
        @JsonProperty("long") String longDescription,
        @JsonProperty("short") String shortDescription,
        Integer elapsed,
        Integer extra
) {
}
