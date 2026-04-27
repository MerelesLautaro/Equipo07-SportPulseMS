package com.Equipo07_SportPulseMS.ms_notifications.controller;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.ErrorResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCancelResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionResponse;
import com.Equipo07_SportPulseMS.ms_notifications.service.customer.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Subscriptions", description = "Gestión de suscripciones a eventos deportivos")
public class NotificationController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Crear suscripción",
            description = "Permite crear una suscripción a eventos de un fixture o equipo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Suscripción creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionCreateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "error": "VALIDATION_ERROR",
                              "message": "teamId is required",
                              "timestamp": "2026-04-27T08:36:30.992Z"
                            }
                            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Suscripción duplicada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionCreateResponse> createSubscription(
            @RequestBody @Valid CreateSubscriptionRequest request) {

        SubscriptionCreateResponse response = subscriptionService.createSubscription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Obtener suscripciones activas",
            description = "Retorna todas las suscripciones activas del usuario autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de suscripciones",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionResponse.class))
                    )
            )
    })
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUserLogged());
    }

    @Operation(
            summary = "Cancelar suscripción",
            description = "Cancela una suscripción existente",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Suscripción cancelada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionCancelResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "error": "ACCESS_DENIED",
                              "message": "Esta subscripcion no te pertenece",
                              "timestamp": "2026-04-27T08:36:30.992Z"
                            }
                            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Suscripción no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "error": "SUBSCRIPTION_NOT_FOUND",
                              "message": "Subscripción no encontrada",
                              "timestamp": "2026-04-27T08:39:20.421Z"
                            }
                            """)
                    )
            )
    })
    @DeleteMapping("/subscribe/{subscriptionId}")
    public ResponseEntity<SubscriptionCancelResponse> cancelSubscription(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable UUID subscriptionId) {

        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId));
    }
}