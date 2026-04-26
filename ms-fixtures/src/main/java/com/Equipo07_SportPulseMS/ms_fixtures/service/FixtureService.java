package com.Equipo07_SportPulseMS.ms_fixtures.service;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureFilters;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLiveResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;

import java.util.List;

public interface FixtureService {

    List<FixtureResponse> filterFixtureBy(FixtureFilters filters);

    List<FixtureLiveResponse> getFixtureInLive();

    List<FixtureEventResponse> getFixtureById(int fixtureId);
}
