package com.Equipo07_SportPulseMS.ms_notifications.entity;

import java.util.Map;

public enum NotificationEvent {

    MATCH_START,
    GOAL,
    MATCH_END,
    CARD;

    private static final Map<String, NotificationEvent> MAPPING = Map.ofEntries(
            Map.entry("goal", GOAL),
            Map.entry("card", CARD),
            Map.entry("match start", MATCH_START),
            Map.entry("match end", MATCH_END)
    );

    public static NotificationEvent from(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }

        return MAPPING.get(type.trim().toLowerCase());
    }
}