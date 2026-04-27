package com.Equipo07_SportPulseMS.ms_leagues.controller;

import com.Equipo07_SportPulseMS.ms_leagues.exception.LeagueNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(LeagueNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLeagueNotFound(LeagueNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "LEAGUE_NOT_FOUND",
                        "message", "No existe una liga con el ID proporcionado",
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
