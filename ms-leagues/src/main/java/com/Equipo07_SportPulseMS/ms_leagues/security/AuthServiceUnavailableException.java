package com.Equipo07_SportPulseMS.ms_leagues.security;

public class AuthServiceUnavailableException extends RuntimeException {

    public AuthServiceUnavailableException() {
        super("No se pudo validar el token con ms-auth");
    }
}
