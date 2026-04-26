package com.Equipo07_SportPulseMS.ms_fixtures.dto;

public enum FixtureStatus {
    NS, // No Started
    LIVE,
    FT; // Finished

    public boolean matches(String status) {
        return this.name().equalsIgnoreCase(status);
    }
}
