package de.lray.service.admin.user.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

class AgeRangeConstraintValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final BirthdayDto target = new BirthdayDto();

    @ParameterizedTest
    @MethodSource("validDateArguments")
    @NullSource
    void whenDateNullOrValid_thenValid(LocalDate item) {
        // Given
        target.birthDate = item;
        // When
        Set<ConstraintViolation<BirthdayDto>> constraintViolations = validator
                .validate(target);
        // Then
        assertNoViolation(constraintViolations);
    }

    @ParameterizedTest
    @MethodSource("invalidDateArguments")
    void whenDateOutsideRange_thenFail(LocalDate item) {
        // Given
        target.birthDate = item;
        // When
        Set<ConstraintViolation<BirthdayDto>> constraintViolations = validator
                .validate(target);
        // Then
        Assertions.assertThat(
                constraintViolations.stream().map(ConstraintViolation::getMessage))
                .containsOnly(AgeRangeConstraint.DEFAULT_MESSAGE);
    }

    private static Stream<Arguments> validDateArguments() {
        return Stream.of(
                Arguments.of(LocalDate.now()),
                Arguments.of(LocalDate.now().minusYears(AgeRangeConstraint.MAX_AGE).plusDays(1)),
                Arguments.of(LocalDate.now().minusYears(AgeRangeConstraint.MAX_AGE))
        );
    }

    private static Stream<Arguments> invalidDateArguments() {
        return Stream.of(
                Arguments.of(LocalDate.now().plusDays(1)),
                Arguments.of(LocalDate.now().minusYears(AgeRangeConstraint.MAX_AGE).minusDays(1))
        );
    }

    private void assertNoViolation(Set<ConstraintViolation<AgeRangeConstraintValidatorTest.BirthdayDto>> constraintViolations) {
        Assertions.assertThat(
                constraintViolations.stream().map(ConstraintViolation::getMessage))
                .isEmpty();
    }

    static class BirthdayDto {
        @AgeRangeConstraint()
        public LocalDate birthDate;
    }
}

