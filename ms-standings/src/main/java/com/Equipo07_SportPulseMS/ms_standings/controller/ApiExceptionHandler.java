package com.Equipo07_SportPulseMS.ms_standings.controller;

import com.Equipo07_SportPulseMS.ms_standings.exception.StandingsNotFoundException;
import com.Equipo07_SportPulseMS.ms_standings.exception.TeamStandingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(StandingsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStandingsNotFound(StandingsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "STANDINGS_NOT_FOUND",
                        "message", ex.getMessage(),
                        "timestamp", Instant.now().toString()
                ));
    }

    @ExceptionHandler(TeamStandingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTeamStandingNotFound(TeamStandingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "TEAM_STANDING_NOT_FOUND",
                        "message", ex.getMessage(),
                        "timestamp", Instant.now().toString()
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "INVALID_REQUEST",
                        "message", "Los parametros league y season son requeridos",
                        "timestamp", Instant.now().toString()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "INTERNAL_ERROR",
                        "message", "Error interno procesando la solicitud",
                        "timestamp", Instant.now().toString()
                ));
    }
}
