package com.Equipo07_SportPulseMS.ms_notifications.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType type;

    private Integer teamId;

    private Integer fixtureId;

    @ElementCollection
    @CollectionTable(
            name = "subscription_events",
            joinColumns = @JoinColumn(name = "subscription_id")
    )
    @Column(name = "event")
    @Enumerated(EnumType.STRING)
    private List<NotificationEvent> events;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    private String webhookUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
