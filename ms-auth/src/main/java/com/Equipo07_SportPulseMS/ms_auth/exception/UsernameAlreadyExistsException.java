package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {

    public UsernameAlreadyExistsException() {
        super(
                "USERNAME_ALREADY_EXISTS",
                "Ya existe un usuario con ese nombre de usuario",
                HttpStatus.CONFLICT
        );
    }
}