package de.lray.service.admin.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordConstraintsValidator implements ConstraintValidator<PasswordConstraints, String> {

    private static final Pattern FIND_A_LETTER_PATTERN = Pattern.compile("[A-Za-z]");
    private static final Pattern FIND_A_DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern FIND_A_SPECIAL_CHAR_PATTERN = Pattern.compile("[^A-Za-z0-9]");

    boolean containsDigits = true;
    boolean containsSpecialChar = true;

    @Override
    public void initialize(PasswordConstraints constraintAnnotation) {
        containsDigits = constraintAnnotation.containsDigits();
        containsSpecialChar = constraintAnnotation.containsSpecialChar();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || (
                containsLetters(s)
                        && (!containsDigits || containsDigits(s))
                        && (!containsSpecialChar || containsSpecialChar(s))
        );
    }

    private boolean containsDigits(String currentPW) {
        return currentPW.length() > removeAllMatches(currentPW, FIND_A_DIGIT_PATTERN).length();
    }

    private boolean containsLetters(String currentPW) {
        return currentPW.length() > removeAllMatches(currentPW, FIND_A_LETTER_PATTERN).length();
    }

    private boolean containsSpecialChar(String currentPW) {
        return currentPW.length() > removeAllMatches(currentPW, FIND_A_SPECIAL_CHAR_PATTERN).length();
    }

    private String removeAllMatches(String currentPW, Pattern findASpecialCharPattern) {
        return findASpecialCharPattern.matcher(currentPW).replaceAll("");
    }
}
