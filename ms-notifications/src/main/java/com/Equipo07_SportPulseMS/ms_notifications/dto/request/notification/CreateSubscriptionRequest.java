package com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification;

import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationEvent;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateSubscriptionRequest(

        @NotNull(message = "type is required")
        SubscriptionType type,

        @Positive(message = "teamId must be positive")
        Integer teamId,

        @Positive(message = "fixtureId must be positive")
        Integer fixtureId,

        @NotEmpty(message = "events cannot be empty")
        List<NotificationEvent> events,

        @NotNull(message = "channel is required")
        NotificationChannel channel,

        String webhookUrl

) {}