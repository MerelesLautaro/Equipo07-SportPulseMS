package com.Equipo07_SportPulseMS.ms_notifications.controller;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCancelResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionResponse;
import com.Equipo07_SportPulseMS.ms_notifications.service.customer.SubscriptionService;
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
public class NotificationController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionCreateResponse> createSubscription(@RequestBody @Valid CreateSubscriptionRequest request) {

        SubscriptionCreateResponse response = subscriptionService.createSubscription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUserLogged());
    }

    @DeleteMapping("/subscribe/{subscriptionId}")
    public ResponseEntity<SubscriptionCancelResponse> cancelSubscription(@PathVariable UUID subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId));
    }
}
