package com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification;

import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionCancelResponse(
        UUID subscriptionId,
        SubscriptionStatus status,
        Instant cancelledAt
) {}