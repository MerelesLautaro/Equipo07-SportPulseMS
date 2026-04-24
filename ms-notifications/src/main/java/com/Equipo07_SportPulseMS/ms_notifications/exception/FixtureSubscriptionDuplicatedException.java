package com.Equipo07_SportPulseMS.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class FixtureSubscriptionDuplicatedException extends ApiException {

    public FixtureSubscriptionDuplicatedException() {
        super(
                "FIXTURE_SUBSCRIPTION_DUPLICATED",
                "El usuario ya tiene una suscripción activa para el mismo fixture y canal",
                HttpStatus.CONFLICT
        );
    }
}
