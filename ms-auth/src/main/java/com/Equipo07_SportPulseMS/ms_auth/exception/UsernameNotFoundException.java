package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends ApiException {

    public UsernameNotFoundException() {
        super(
                "USERNAME_NOT_FOUND",
                "No existe un usuario con ese username",
                HttpStatus.NOT_FOUND
        );
    }
}
