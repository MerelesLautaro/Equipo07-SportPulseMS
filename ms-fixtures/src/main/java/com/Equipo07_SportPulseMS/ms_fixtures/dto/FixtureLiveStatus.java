package com.Equipo07_SportPulseMS.ms_fixtures.dto;

public enum FixtureLiveStatus {
    ONE_HOUR("1H"),
    HALF_TIME("HT"),
    TWO_HOURS("2H");

    private String value;

    FixtureLiveStatus(String value) {
        this.value = value;
    }

}
