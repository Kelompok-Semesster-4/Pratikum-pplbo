package com.library.app.model.enums;

public enum FeedbackStatus {
    NEW,
    READ,
    RESPONDED;

    public static FeedbackStatus fromDbValue(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return NEW;
        }

        try {
            return FeedbackStatus.valueOf(rawValue.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return NEW;
        }
    }
}