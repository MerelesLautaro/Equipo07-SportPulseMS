package com.Equipo07_SportPulseMS.ms_teams.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        super();
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}
