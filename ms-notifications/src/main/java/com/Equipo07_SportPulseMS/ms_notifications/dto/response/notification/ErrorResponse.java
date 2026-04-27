package com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Estructura estándar de errores")
public record ErrorResponse(

        @Schema(example = "ACCESS_DENIED")
        String error,

        @Schema(example = "Esta subscripcion no te pertenece")
        String message,

        @Schema(example = "2026-04-27T08:36:30.992Z")
        Instant timestamp

) {}
