package de.lray.service.admin.user.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

class PasswordConstraintsValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final FullConstrainDto target = new FullConstrainDto();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"k@rmA-pa$$w0r|)"})
    void whenSecretNullOrValid_thenValid(String secret) throws Exception {
        // Given
        target.secret = secret;
        // When
        Set<ConstraintViolation<FullConstrainDto>> constraintViolations = validator
                .validate(target);
        // Then
        assertNoViolation(constraintViolations);
    }

    @ParameterizedTest
    @ValueSource(strings = {"testPasswort", "12345", "test12345", "!@#$%1324"})
    void whenLetterOnly_thenFail(String secret) throws Exception {
        // Given
        target.secret = secret;
        // When
        Set<ConstraintViolation<FullConstrainDto>> constraintViolations = validator
                .validate(target);
        // Then
        Assertions.assertThat(
                constraintViolations.stream().map(ConstraintViolation::getMessage))
                .containsOnly("Password not compliant");
    }

    @Test
    void whenDigitConstrainsReduced_thenValid() throws Exception {
        // Given
        var reducedTarget = new NoDigitConstrainDto();
        reducedTarget.secret = "!ABcde";
        // When
        Set<ConstraintViolation<NoDigitConstrainDto>> constraintViolations = validator
                .validate(reducedTarget);
        // Then
        assertNoViolation(constraintViolations);
    }

    @Test
    void whenDigitConstrainsReduced_failOnOtherConstraintViolation() throws Exception {
        // Given
        var reducedTarget = new NoDigitConstrainDto();
        reducedTarget.secret = "!!!!!!!!";
        // When
        Set<ConstraintViolation<NoDigitConstrainDto>> constraintViolations = validator
                .validate(reducedTarget);
        // Then
        Assertions.assertThat(
                constraintViolations.stream().map(ConstraintViolation::getMessage))
                .containsOnly("Password not compliant");
    }

    @Test
    void whenSpecialCharConstrainsReduced_thenValid() throws Exception {
        // Given
        var reducedTarget = new NoSpecialCharConstrainDto();
        reducedTarget.secret = "1ABcde";
        // When
        Set<ConstraintViolation<NoSpecialCharConstrainDto>> constraintViolations = validator
                .validate(reducedTarget);
        // Then
        assertNoViolation(constraintViolations);
    }

    @Test
    void whenSpecialCharConstrainsReduced_failOnOtherConstraintViolation() throws Exception {
        // Given
        var reducedTarget = new NoDigitConstrainDto();
        reducedTarget.secret = "abcdefghijkl";
        // When
        Set<ConstraintViolation<NoDigitConstrainDto>> constraintViolations = validator
                .validate(reducedTarget);
        // Then
        Assertions.assertThat(
                constraintViolations.stream().map(ConstraintViolation::getMessage))
                .containsOnly("Password not compliant");
    }


    private void assertNoViolation(Set constraintViolations) {
        Assertions.assertThat(
                constraintViolations.stream().map(t -> ((ConstraintViolation) t).getMessage()))
                .isEmpty();
    }

    static class FullConstrainDto {
        @PasswordConstraints(message = "Password not compliant")
        public String secret;
    }

    static class NoDigitConstrainDto {
        @PasswordConstraints(message = "Password not compliant", containsDigits = false)
        public String secret;
    }

    static class NoSpecialCharConstrainDto {
        @PasswordConstraints(message = "Password not compliant", containsSpecialChar = false)
        public String secret;
    }
}

