package com.Equipo07_SportPulseMS.ms_fixtures.mapper;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureItem;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLeagueResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLiveResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureStatusResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureTeamResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureVenueResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.League;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.Status;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.Team;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FixtureMapper {

    @Mapping(target = "id", source = "fixture.id")
    @Mapping(target = "date", source = "fixture.date")
    @Mapping(target = "status", source = "fixture.status")
    @Mapping(target = "league", source = "league")
    @Mapping(target = "homeTeam", expression = "java(mapTeam(item.teams() != null ? item.teams().home() : null, item.goals() != null ? item.goals().home() : null))")
    @Mapping(target = "awayTeam", expression = "java(mapTeam(item.teams() != null ? item.teams().away() : null, item.goals() != null ? item.goals().away() : null))")
    @Mapping(target = "venue", source = "fixture.venue")
    FixtureResponse toFixtureResponse(FixtureItem item);

    @Mapping(target = "id", source = "fixture.id")
    @Mapping(target = "elapsed", source = "fixture.status.elapsed")
    @Mapping(target = "status", source = "fixture.status")
    @Mapping(target = "league", source = "league")
    @Mapping(target = "homeTeam", expression = "java(mapTeam(item.teams() != null ? item.teams().home() : null, item.goals() != null ? item.goals().home() : null))")
    @Mapping(target = "awayTeam", expression = "java(mapTeam(item.teams() != null ? item.teams().away() : null, item.goals() != null ? item.goals().away() : null))")
    FixtureLiveResponse toFixtureLiveResponse(FixtureItem item);

    default FixtureTeamResponse mapTeam(Team team, Integer goals) {
        if (team == null) {
            return null;
        }
        return new FixtureTeamResponse(team.id(), team.name(), team.logo(), goals);
    }

    default FixtureStatusResponse map(Status status) {
        if (status == null) {
            return null;
        }
        return new FixtureStatusResponse(status.shortDescription(), status.longDescription());
    }

    default FixtureLeagueResponse map(League league) {
        if (league == null) {
            return null;
        }
        return new FixtureLeagueResponse(league.id(), league.name(), league.round());
    }

    default FixtureVenueResponse map(Venue venue) {
        if (venue == null) {
            return null;
        }
        return new FixtureVenueResponse(venue.name(), venue.city());
    }
}
