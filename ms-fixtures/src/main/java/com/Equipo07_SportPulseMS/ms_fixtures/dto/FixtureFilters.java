package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.Equipo07_SportPulseMS.ms_fixtures.exception.InvalidFixtureFilterException;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record FixtureFilters(
        Integer league,
        Integer team,
        LocalDate date,
        FixtureStatus status
) {
    /**
     * Este es un valor hardcodeado para el ejemplo.
     * Esto se debe a que la API de football solo permite
     * pasar valores entre 2022 y 2024 en el plan gratuito
     */
    public static final int SEASON_AVAILABLE_ON_THE_FOOTBALL_API_FREE_PLAN = 2024;

    public String dateString() {
        if (this.team == null && this.league == null && this.date == null) {
            return LocalDate.now().toString();
        }

        return this.date != null ? this.date.toString() : null;
    }

    public String statusString() {
        return this.status != null ? this.status.name() : null;
    }

    public Integer season(boolean isFreePlan) {
        if (this.team == null && this.league == null) return null;

        if (this.date != null) return this.date.getYear();

        if (isFreePlan) return SEASON_AVAILABLE_ON_THE_FOOTBALL_API_FREE_PLAN;

        return LocalDate.now().getYear();
    }

    public static FixtureFilters fromRequestParams(Integer league, Integer team, String date, String status) {
        return new FixtureFilters(
                league,
                team,
                parseDate(date),
                parseStatus(status)
        );
    }

    private static LocalDate parseDate(String date) {
        if (date == null) return null;

        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ex) {
            throw new InvalidFixtureFilterException("Invalid date format. Expected format is YYYY-MM-DD.");
        }
    }

    private static FixtureStatus parseStatus(String status) {
        if (status == null) return null;

        try {
            return FixtureStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidFixtureFilterException(
                    "Invalid fixture status. Allowed values are NS, LIVE, FT (case-insensitive)."
            );
        }
    }

}