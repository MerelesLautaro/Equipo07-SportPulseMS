package com.Equipo07_SportPulseMS.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class InvalidSubscriptionRequestException extends ApiException {

    public InvalidSubscriptionRequestException(String message) {
        super(
                "INVALID_SUBSCRIPTION_REQUEST",
                message,
                HttpStatus.BAD_REQUEST
        );
    }
}