package com.wde.eventplanner.constants;

public class RegexConstants {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String HAS_DIGIT_REGEX = ".*\\d.*";
    public static final String HAS_SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String NAME_REGEX = "^[A-Za-z ]{1,50}$";
    public static final String CITY_REGEX = "^[A-Za-z ]{1,50}$";
    public static final String ADDRESS_REGEX = "^[A-Za-z0-9 ,.-]{5,100}$"; // allows alphanumeric characters and common address symbols
    public static final String PHONE_REGEX = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{4}$";

    public static boolean isStrongPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) return false;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(HAS_DIGIT_REGEX);
        boolean hasSpecialChar = password.matches(HAS_SPECIAL_CHAR_REGEX);

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    private RegexConstants() {
    }
}
