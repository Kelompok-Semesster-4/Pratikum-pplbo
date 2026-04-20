package com.library.app.model.enums;

public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static RequestStatus fromDbValue(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return PENDING;
        }

        try {
            return RequestStatus.valueOf(rawValue.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return PENDING;
        }
    }
}