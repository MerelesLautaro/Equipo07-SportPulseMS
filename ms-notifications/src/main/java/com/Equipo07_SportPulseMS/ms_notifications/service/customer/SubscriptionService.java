package com.Equipo07_SportPulseMS.ms_notifications.service.customer;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;

public interface SubscriptionService {
    SubscriptionCreateResponse createSubscription(CreateSubscriptionRequest createSubscriptionRequest);
}
