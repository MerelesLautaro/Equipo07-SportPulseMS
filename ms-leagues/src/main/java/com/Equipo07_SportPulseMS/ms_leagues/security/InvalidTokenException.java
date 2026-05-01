package com.Equipo07_SportPulseMS.ms_leagues.security;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Token JWT invalido o expirado");
    }
}
