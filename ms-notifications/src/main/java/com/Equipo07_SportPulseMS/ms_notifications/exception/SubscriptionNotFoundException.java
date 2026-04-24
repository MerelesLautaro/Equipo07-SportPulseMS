package com.Equipo07_SportPulseMS.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends ApiException {

    public SubscriptionNotFoundException() {
        super(
                "SUBSCRIPTION_NOT_FOUND",
                "Subscripción no encontrada",
                HttpStatus.NOT_FOUND
        );
    }
}
