package com.Equipo07_SportPulseMS.ms_teams.util.mapper;

import com.Equipo07_SportPulseMS.ms_teams.dto.StadiumResponse;
import com.Equipo07_SportPulseMS.ms_teams.dto.TeamItem;
import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;
import com.Equipo07_SportPulseMS.ms_teams.dto.Venue;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    /**
     * el argumento isNational en createTeamResponse se deja nulo para que Jackson no serialize este valor
     * en el endpoint GET /api/teams. Leer el comentario en TeamResponse
     */
    public TeamResponse toTeamResponseWithoutNational(TeamItem teamItem) {
        return this.createTeamResponse(teamItem, null);
    }

    public TeamResponse toTeamResponse(TeamItem teamItem) {
        return this.createTeamResponse(teamItem, teamItem.team().national());
    }

    private TeamResponse createTeamResponse(TeamItem teamItem, Boolean isNational) {
        var stadium = this.createStadium(teamItem.venue());
        return new TeamResponse(
                teamItem.team().id(),
                teamItem.team().name(),
                teamItem.team().country(),
                teamItem.team().logo(),
                teamItem.team().founded(),
                isNational,
                stadium
        );
    }

    private StadiumResponse createStadium(Venue venue) {
        return new StadiumResponse(
                venue.name(),
                venue.address(),
                venue.city(),
                venue.capacity(),
                venue.surface());
    }
}