package com.Equipo07_SportPulseMS.ms_notifications.repository;

import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationChannel;
import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByUserIdAndTypeAndTeamIdAndChannelAndStatus(
            UUID userId,
            SubscriptionType type,
            Integer teamId,
            NotificationChannel channel,
            SubscriptionStatus status
    );

    boolean existsByUserIdAndTypeAndFixtureIdAndChannelAndStatus(
            UUID userId,
            SubscriptionType type,
            Integer fixtureId,
            NotificationChannel channel,
            SubscriptionStatus status
    );

    @Query("""
        SELECT DISTINCT s FROM Subscription s
        LEFT JOIN FETCH s.events
        WHERE s.status = :status
    """)
    List<Subscription> findActive(SubscriptionStatus status);

    @Query("""
        SELECT DISTINCT s FROM Subscription s
        LEFT JOIN FETCH s.events
        WHERE s.userId = :userId
        AND s.status = :status
    """)
    List<Subscription> findByUserIdAndStatus(UUID userId, SubscriptionStatus status);
}
