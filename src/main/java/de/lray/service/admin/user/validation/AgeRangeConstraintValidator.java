package de.lray.service.admin.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeRangeConstraintValidator implements ConstraintValidator<AgeRangeConstraint, LocalDate> {

    int minYears;
    int maxYears;

    @Override
    public void initialize(AgeRangeConstraint constraintAnnotation) {
        minYears = constraintAnnotation.minYears();
        maxYears = constraintAnnotation.maxYears();
    }

    @Override
    public boolean isValid(LocalDate s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || nullsafeWithinRange(s);
    }

    private boolean nullsafeWithinRange(LocalDate s) {
        var currentDate = LocalDate.now();
        return !(
                s.isBefore(currentDate.minusYears(maxYears))
                        || s.isAfter(currentDate.minusYears(minYears))
        );
    }
}
