package com.Equipo07_SportPulseMS.ms_notifications.service.customer.impl;

import com.Equipo07_SportPulseMS.ms_notifications.dto.request.notification.CreateSubscriptionRequest;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCreateResponse;
import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import com.Equipo07_SportPulseMS.ms_notifications.exception.FixtureSubscriptionDuplicatedException;
import com.Equipo07_SportPulseMS.ms_notifications.exception.InvalidSubscriptionRequestException;
import com.Equipo07_SportPulseMS.ms_notifications.exception.TeamSubscriptionDuplicatedException;
import com.Equipo07_SportPulseMS.ms_notifications.repository.SubscriptionRepository;
import com.Equipo07_SportPulseMS.ms_notifications.service.customer.SubscriptionService;
import com.Equipo07_SportPulseMS.ms_notifications.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return mapToResponse(saved);
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

    private SubscriptionCreateResponse mapToResponse(Subscription saved) {
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
}
