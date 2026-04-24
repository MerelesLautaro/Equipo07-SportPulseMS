package com.Equipo07_SportPulseMS.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException {

    public AccessDeniedException(){
        super(
                "ACCESS_DENIED",
                "Esta subscripcion no te pertenece",
                HttpStatus.FORBIDDEN
        );
    }
}
