package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FixtureVenueResponse(
        String name,
        String city
) {
    public static FixtureVenueResponse fromVenue(Venue venue) {
        if (venue == null) {
            return null;
        }

        return new FixtureVenueResponse(venue.name(), venue.city());
    }
}
