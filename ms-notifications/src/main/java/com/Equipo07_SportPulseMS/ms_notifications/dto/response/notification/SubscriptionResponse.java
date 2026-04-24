package com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification;

import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationEvent;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SubscriptionResponse(

        UUID subscriptionId,
        SubscriptionType type,
        Integer teamId,
        Integer fixtureId,
        List<NotificationEvent> events,
        NotificationChannel channel,
        String webhookUrl,
        SubscriptionStatus status

) {}