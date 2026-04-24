package com.Equipo07_SportPulseMS.ms_notifications.service.processor;

import com.Equipo07_SportPulseMS.ms_notifications.entity.NotificationEvent;
import com.Equipo07_SportPulseMS.ms_notifications.entity.Subscription;
import com.Equipo07_SportPulseMS.ms_notifications.entity.SubscriptionType;
import com.Equipo07_SportPulseMS.ms_notifications.service.client.FixturesClient;
import com.Equipo07_SportPulseMS.ms_notifications.service.state.SubscriptionStateService;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessorService {

    private final FixturesClient fixturesClient;
    private final SubscriptionStateService stateService;

    public void process(Subscription subscription) {

        if (subscription.getType() == SubscriptionType.FIXTURE) {
            processFixture(subscription);
        } else {
            processTeam(subscription);
        }
    }

    // =========================
    // FIXTURE
    // =========================
    private void processFixture(Subscription subscription) {

        Integer fixtureId = subscription.getFixtureId();

        List<FixtureEventResponse> events =
                fixturesClient.getFixtureEvents(fixtureId);

        processEvents(subscription, fixtureId, events);
    }

    // =========================
    // TEAM
    // =========================
    private void processTeam(Subscription subscription) {

        Integer teamId = subscription.getTeamId();

        List<FixtureResponse> fixtures =
                fixturesClient.getFixturesByTeam(teamId, "LIVE");

        for (FixtureResponse fixture : fixtures) {

            Integer fixtureId = fixture.id();

            List<FixtureEventResponse> events =
                    fixturesClient.getFixtureEvents(fixtureId);

            processEvents(subscription, fixtureId, events);
        }
    }

    // =========================
    // CORE LOGIC
    // =========================
    private void processEvents(Subscription subscription,
                               Integer fixtureId,
                               List<FixtureEventResponse> events) {

        int currentCount = events.size();

        int lastCount = stateService
                .getLastEventCount(subscription.getId(), fixtureId);

        if (currentCount <= lastCount) {
            return;
        }

        List<FixtureEventResponse> newEvents =
                events.subList(lastCount, currentCount);

        for (FixtureEventResponse event : newEvents) {

            NotificationEvent mappedEvent =
                    NotificationEvent.from(event.type());

            if (mappedEvent == null) {
                continue;
            }

            if (!subscription.getEvents().contains(mappedEvent)) {
                continue;
            }

            logEvent(subscription, fixtureId, event, mappedEvent);
        }

        stateService.saveLastEventCount(
                subscription.getId(),
                fixtureId,
                currentCount
        );
    }

    // =========================
    // LOG (notification)
    // =========================
    private void logEvent(Subscription subscription,
                          Integer fixtureId,
                          FixtureEventResponse event,
                          NotificationEvent mappedEvent) {

        log.info(
                "[NOTIFICATION] user={} | sub={} | fixture={} | event={} | minute={} | detail={}",
                subscription.getUserId(),
                subscription.getId(),
                fixtureId,
                mappedEvent,
                event.elapsed(),
                event.detail()
        );
    }
}