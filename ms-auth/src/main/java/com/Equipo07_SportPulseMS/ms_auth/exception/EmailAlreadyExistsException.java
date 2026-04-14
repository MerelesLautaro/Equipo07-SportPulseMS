package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {

    public EmailAlreadyExistsException() {
        super(
                "USER_ALREADY_EXISTS",
                "Ya existe un usuario con ese email",
                HttpStatus.CONFLICT
        );
    }
}