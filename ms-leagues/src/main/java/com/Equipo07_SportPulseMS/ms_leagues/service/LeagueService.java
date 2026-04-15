package com.Equipo07_SportPulseMS.ms_leagues.service;

import com.Equipo07_SportPulseMS.ms_leagues.client.ApiSportsLeaguesClient;
import com.Equipo07_SportPulseMS.ms_leagues.client.dto.ApiSportsLeagueItem;
import com.Equipo07_SportPulseMS.ms_leagues.client.dto.ApiSportsLeaguesEnvelope;
import com.Equipo07_SportPulseMS.ms_leagues.dto.LeagueSummaryResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class LeagueService {

    private final ApiSportsLeaguesClient apiSportsLeaguesClient;

    public LeagueService(ApiSportsLeaguesClient apiSportsLeaguesClient) {
        this.apiSportsLeaguesClient = apiSportsLeaguesClient;
    }

    @Cacheable(value = "leaguesByFilters", key = "T(String).valueOf(#country).concat('|').concat(T(String).valueOf(#season))")
    public List<LeagueSummaryResponse> getLeagues(String country, Integer season) {
        ApiSportsLeaguesEnvelope envelope = apiSportsLeaguesClient.getLeagues(null, country, season);
        if (envelope == null || envelope.response() == null) {
            return Collections.emptyList();
        }

        return envelope.response().stream()
                .filter(item -> matchesCountry(item, country))
                .filter(item -> matchesSeason(item, season))
                .map(this::toSummary)
                .toList();
    }

    private boolean matchesCountry(ApiSportsLeagueItem item, String country) {
        if (country == null || country.isBlank()) {
            return true;
        }
        return item.country() != null
                && item.country().name() != null
                && item.country().name().equalsIgnoreCase(country);
    }

    private boolean matchesSeason(ApiSportsLeagueItem item, Integer season) {
        if (season == null) {
            return true;
        }
        return item.seasons() != null && item.seasons().stream()
                .anyMatch(s -> season.equals(s.year()));
    }

    private LeagueSummaryResponse toSummary(ApiSportsLeagueItem item) {
        ApiSportsLeagueItem.Season currentSeason = item.seasons() == null
                ? null
                : item.seasons().stream()
                .filter(s -> Boolean.TRUE.equals(s.current()))
                .findFirst()
                .orElseGet(() -> item.seasons().stream()
                        .max(Comparator.comparing(ApiSportsLeagueItem.Season::year))
                        .orElse(null));

        return new LeagueSummaryResponse(
                item.league() != null ? item.league().id() : null,
                item.league() != null ? item.league().name() : null,
                item.league() != null ? item.league().type() : null,
                item.country() != null ? item.country().name() : null,
                item.league() != null ? item.league().logo() : null,
                currentSeason != null ? currentSeason.year() : null,
                currentSeason != null ? currentSeason.start() : null,
                currentSeason != null ? currentSeason.end() : null
        );
    }
}
