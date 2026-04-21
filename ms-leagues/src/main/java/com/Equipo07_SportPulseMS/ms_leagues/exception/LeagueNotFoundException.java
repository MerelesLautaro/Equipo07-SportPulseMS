package com.Equipo07_SportPulseMS.ms_leagues.exception;

public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException(Integer leagueId) {
        super("No existe una liga con el ID proporcionado: " + leagueId);
    }
}
