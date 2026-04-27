package com.Equipo07_SportPulseMS.ms_leagues.controller;

import com.Equipo07_SportPulseMS.ms_leagues.exception.LeagueNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void leagueNotFoundMapsTo404AndExpectedErrorCode() {
        var response = handler.handleLeagueNotFound(new LeagueNotFoundException(999));

        assertEquals(404, response.getStatusCode().value());
        assertEquals("LEAGUE_NOT_FOUND", response.getBody().get("error"));
        assertEquals("No existe una liga con el ID proporcionado", response.getBody().get("message"));
    }
}
