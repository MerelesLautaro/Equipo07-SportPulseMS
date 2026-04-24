package com.Equipo07_SportPulseMS.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class TeamSubscriptionDuplicatedException extends ApiException {

    public TeamSubscriptionDuplicatedException() {
        super(
                "TEAM_SUBSCRIPTION_DUPLICATED",
                "El usuario ya tiene una suscripción activa para el mismo equipo y canal",
                HttpStatus.CONFLICT
        );
    }
}
