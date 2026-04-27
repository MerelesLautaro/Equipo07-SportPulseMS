package com.Equipo07_SportPulseMS.ms_notifications.service.scheduler;

import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionStatus;
import com.Equipo07_SportPulseMS.ms_notifications.repository.SubscriptionRepository;
import com.Equipo07_SportPulseMS.ms_notifications.service.client.RateLimitService;
import com.Equipo07_SportPulseMS.ms_notifications.service.processor.NotificationProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    /*
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationProcessorService processorService;
    private final RateLimitService rateLimitService;

    @Scheduled(fixedRate = 300000)
    public void processSubscriptions() {

        if (rateLimitService.isRateLimited()) {
            log.warn("Scheduler pausado por rate limit");
            return;
        }

        log.info("Starting notification scheduler...");

        List<Subscription> activeSubscriptions =
                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        for (Subscription sub : activeSubscriptions) {
            log.info("Processing subscription {}", sub.getId());

            try {
                processorService.process(sub);
            } catch (Exception e) {
                log.error("Error processing subscription {}", sub.getId(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetRateLimit() {
        log.info("Resetting rate limit flag");
        rateLimitService.reset();
    }
    */
}