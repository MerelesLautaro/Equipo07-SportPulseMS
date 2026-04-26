package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FixtureStatusResponse(
        @JsonProperty("short") String shortDescription,
        @JsonProperty("long") String longDescription
) {
    public static FixtureStatusResponse fromStatus(Status status) {
        if (status == null) {
            return null;
        }

        return new FixtureStatusResponse(status.shortDescription(), status.longDescription());
    }
}
