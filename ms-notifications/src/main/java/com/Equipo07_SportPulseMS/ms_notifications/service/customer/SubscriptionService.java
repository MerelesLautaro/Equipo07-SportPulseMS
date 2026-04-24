package com.Equipo07_SportPulseMS.ms_notifications.service.customer;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionCreateResponse createSubscription(CreateSubscriptionRequest createSubscriptionRequest);
    List<SubscriptionResponse> getSubscriptionsByUserLogged();
}
