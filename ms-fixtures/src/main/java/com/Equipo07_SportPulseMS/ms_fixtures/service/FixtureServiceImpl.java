package com.Equipo07_SportPulseMS.ms_fixtures.service;

import com.Equipo07_SportPulseMS.ms_fixtures.api.FixtureClient;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureFilters;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureItem;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLiveResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureStatus;
import com.Equipo07_SportPulseMS.ms_fixtures.exception.FixtureNotFound;
import com.Equipo07_SportPulseMS.ms_fixtures.mapper.FixtureMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixtureServiceImpl implements FixtureService {

    private final FixtureClient fixtureClient;

    @Value("${football.api.free-plan}")
    private boolean isFreePlan;

    @Override
    public List<FixtureResponse> filterFixtureBy(FixtureFilters filters) {
        Integer season = filters.season(this.isFreePlan);

        var fixtures = fixtureClient.getFixturesByFilters(
                filters.league(),
                filters.team(),
                filters.dateString(),
                filters.statusString(),
                season
        );

        return fixtures.response().stream()
                .map(FixtureResponse::fromFixtureItem)
                .toList();
    }

    @Override
    public List<FixtureLiveResponse> getFixtureInLive() {
        var fixtures = fixtureClient.getFixtureInLive("all");
        assert fixtures != null;

        return fixtures.response().stream()
                .filter(item -> this.isMapLiveStatus(item.fixture().status().shortDescription()))
                .map(FixtureLiveResponse::fromFixtureItem)
                .toList();
    }

    @Override
    public List<FixtureEventResponse> getFixtureById(int fixtureId) {
        var fixtures = fixtureClient.getFixtureById(fixtureId);
        assert fixtures != null;

        if (fixtures.response().isEmpty()) {
            throw new FixtureNotFound("Fixture with id " + fixtureId + " not found");
        }

        var events = fixtures.response().get(0).events();

        return events.stream()
                .map(FixtureEventResponse::fromEvent)
                .toList();
    }

    private boolean isMapLiveStatus(String status) {
        return "1H".equals(status) || "2H".equals(status) || "HT".equals(status);
    }
}