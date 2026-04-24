package com.Equipo07_SportPulseMS.ms_notifications.service.customer.impl;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCancelResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionResponse;
import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import com.Equipo07_SportPulseMS.ms_notifications.exception.*;
import com.Equipo07_SportPulseMS.ms_notifications.repository.SubscriptionRepository;
import com.Equipo07_SportPulseMS.ms_notifications.service.customer.SubscriptionService;
import com.Equipo07_SportPulseMS.ms_notifications.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public SubscriptionCreateResponse createSubscription(CreateSubscriptionRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId();

        validateRequestStructure(request);
        validateNoDuplicates(request, userId);

        Subscription subscription = buildSubscription(request, userId);
        Subscription saved = subscriptionRepository.save(subscription);

        return mapToCreateResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptionsByUserLogged() {

        UUID userId = SecurityUtils.getCurrentUserId();

        List<Subscription> subscriptions = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        return subscriptions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public SubscriptionCancelResponse cancelSubscription(UUID subscriptionId) {

        UUID userId = SecurityUtils.getCurrentUserId();

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(SubscriptionNotFoundException::new);

        validateSubscriptionForDelete(subscription, userId);

        subscription.setStatus(SubscriptionStatus.CANCELLED);

        subscriptionRepository.save(subscription);

        return mapToCancelResponse(subscription);
    }

    private void validateSubscriptionForDelete(Subscription subscription, UUID userId) {

        if (!subscription.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new SubscriptionNotFoundException();
        }
    }

    private SubscriptionCancelResponse mapToCancelResponse(Subscription subscription) {
        return new SubscriptionCancelResponse(
                subscription.getId(),
                subscription.getStatus(),
                Instant.now()
        );
    }

    private void validateRequestStructure(CreateSubscriptionRequest request) {

        switch (request.type()) {
            case TEAM -> {
                if (request.teamId() == null) {
                    throw new InvalidSubscriptionRequestException(
                            "teamId is required for TEAM subscriptions"
                    );
                }
            }
            case FIXTURE -> {
                if (request.fixtureId() == null) {
                    throw new InvalidSubscriptionRequestException(
                            "fixtureId is required for FIXTURE subscriptions"
                    );
                }
            }
        }

        if (request.channel() == NotificationChannel.WEBHOOK) {
            if (request.webhookUrl() == null || request.webhookUrl().isBlank()) {
                throw new InvalidSubscriptionRequestException(
                        "webhookUrl is required for WEBHOOK channel"
                );
            }
        }
    }

    private void validateNoDuplicates(CreateSubscriptionRequest request, UUID userId) {

        switch (request.type()) {
            case TEAM -> {
                boolean exists = subscriptionRepository
                        .existsByUserIdAndTypeAndTeamIdAndChannelAndStatus(
                                userId,
                                SubscriptionType.TEAM,
                                request.teamId(),
                                request.channel(),
                                SubscriptionStatus.ACTIVE
                        );

                if (exists) {
                    throw new TeamSubscriptionDuplicatedException();
                }
            }

            case FIXTURE -> {
                boolean exists = subscriptionRepository
                        .existsByUserIdAndTypeAndFixtureIdAndChannelAndStatus(
                                userId,
                                SubscriptionType.FIXTURE,
                                request.fixtureId(),
                                request.channel(),
                                SubscriptionStatus.ACTIVE
                        );

                if (exists) {
                    throw new FixtureSubscriptionDuplicatedException();
                }
            }
        }
    }

    private Subscription buildSubscription(CreateSubscriptionRequest request, UUID userId) {

        Integer teamId = null;
        Integer fixtureId = null;

        if (request.type() == SubscriptionType.TEAM) {
            teamId = request.teamId();
        } else if (request.type() == SubscriptionType.FIXTURE) {
            fixtureId = request.fixtureId();
        }

        return Subscription.builder()
                .userId(userId)
                .type(request.type())
                .teamId(teamId)
                .fixtureId(fixtureId)
                .events(request.events())
                .channel(request.channel())
                .webhookUrl(
                        request.channel() == NotificationChannel.WEBHOOK
                                ? request.webhookUrl()
                                : null
                )
                .status(SubscriptionStatus.ACTIVE)
                .build();
    }

    private SubscriptionCreateResponse mapToCreateResponse(Subscription saved) {
        return new SubscriptionCreateResponse(
                saved.getId(),
                saved.getUserId(),
                saved.getType(),
                saved.getTeamId(),
                saved.getFixtureId(),
                saved.getEvents(),
                saved.getChannel(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    private SubscriptionResponse mapToResponse(Subscription s) {
        return new SubscriptionResponse(
                s.getId(),
                s.getType(),
                s.getTeamId(),
                s.getFixtureId(),
                s.getEvents(),
                s.getChannel(),
                s.getWebhookUrl(),
                s.getStatus()
        );
    }
}
