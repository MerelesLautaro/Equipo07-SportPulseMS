package com.Equipo07_SportPulseMS.ms_notifications.integration.service;

import com.Equipo07_SportPulseMS.ms_notifications.dto.response.notification.SubscriptionCancelResponse;
import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import com.Equipo07_SportPulseMS.ms_notifications.exception.AccessDeniedException;
import com.Equipo07_SportPulseMS.ms_notifications.exception.SubscriptionNotFoundException;
import com.Equipo07_SportPulseMS.ms_notifications.repository.SubscriptionRepository;
import com.Equipo07_SportPulseMS.ms_notifications.service.client.FixturesClient;
import com.Equipo07_SportPulseMS.ms_notifications.service.customer.SubscriptionService;
import com.Equipo07_SportPulseMS.ms_notifications.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SubscriptionServiceCancelIT {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private FixturesClient fixturesClient;

    private UUID userId;

    @BeforeEach
    void setUp() {
        subscriptionRepository.deleteAll();
        userId = UUID.randomUUID();
    }

    @Test
    void shouldCancelSubscriptionSuccessfully() {

        // given
        Subscription subscription = Subscription.builder()
                .userId(userId)
                .type(SubscriptionType.FIXTURE)
                .fixtureId(1001)
                .channel(NotificationChannel.LOG)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.save(subscription);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            // when
            SubscriptionCancelResponse response =
                    subscriptionService.cancelSubscription(subscription.getId());

            // then
            Subscription updated = subscriptionRepository
                    .findById(subscription.getId())
                    .orElseThrow();

            assertEquals(SubscriptionStatus.CANCELLED, updated.getStatus());
            assertEquals(subscription.getId(), response.subscriptionId());
            assertEquals(SubscriptionStatus.CANCELLED, response.status());
        }
    }

    @Test
    void shouldThrowWhenSubscriptionNotFound() {

        UUID fakeId = UUID.randomUUID();

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            assertThrows(
                    SubscriptionNotFoundException.class,
                    () -> subscriptionService.cancelSubscription(fakeId)
            );
        }
    }

    @Test
    void shouldThrowAccessDeniedWhenUserDoesNotOwnSubscription() {

        Subscription subscription = Subscription.builder()
                .userId(UUID.randomUUID()) // otro usuario
                .type(SubscriptionType.FIXTURE)
                .fixtureId(1001)
                .channel(NotificationChannel.LOG)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.save(subscription);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            assertThrows(
                    AccessDeniedException.class,
                    () -> subscriptionService.cancelSubscription(subscription.getId())
            );
        }
    }

    @Test
    void shouldThrowWhenSubscriptionAlreadyCancelled() {

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .type(SubscriptionType.FIXTURE)
                .fixtureId(1001)
                .channel(NotificationChannel.LOG)
                .status(SubscriptionStatus.CANCELLED)
                .build();

        subscriptionRepository.save(subscription);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            assertThrows(
                    SubscriptionNotFoundException.class,
                    () -> subscriptionService.cancelSubscription(subscription.getId())
            );
        }
    }
}
