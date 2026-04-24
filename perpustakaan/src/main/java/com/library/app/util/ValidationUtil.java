package com.library.app.util;

import java.time.Year;

public final class ValidationUtil {
    private static final String ISBN_ALLOWED_REGEX = "[0-9-]+";

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNumeric(String value, String message) {
        requireNotBlank(value, message);
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireStudentOrLecturerCode(String value, String message) {
        requireNumeric(value, message);
        if (value.length() < 5 || value.length() > 20) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePublicationYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1900 || year > currentYear + 1) {
            throw new IllegalArgumentException("Tahun terbit tidak valid.");
        }
    }

    public static void requirePositive(int number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static String filterIsbnInput(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value.replaceAll("[^0-9-]", "");
    }

    public static void requireIsbnCharacters(String value, String requiredMessage, String invalidMessage) {
        requireNotBlank(value, requiredMessage);
        if (!value.trim().matches(ISBN_ALLOWED_REGEX)) {
            throw new IllegalArgumentException(invalidMessage);
        }
    }

    public static void requireOptionalIsbnCharacters(String value, String invalidMessage) {
        if (isBlank(value)) {
            return;
        }
        if (!value.trim().matches(ISBN_ALLOWED_REGEX)) {
            throw new IllegalArgumentException(invalidMessage);
        }
    }
}
