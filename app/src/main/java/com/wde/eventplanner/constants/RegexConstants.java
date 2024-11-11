package com.wde.eventplanner.constants;

public class RegexConstants {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String HAS_DIGIT_REGEX = ".*\\d.*";
    public static final String HAS_SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
    public static final int MIN_PASSWORD_LENGTH = 8;
    private RegexConstants() {
    }
}
