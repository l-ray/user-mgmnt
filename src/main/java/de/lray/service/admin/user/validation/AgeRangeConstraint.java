package de.lray.service.admin.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {AgeRangeConstraintValidator.class})
@Documented
public @interface AgeRangeConstraint {

    /* defaults to oldest human known as of time of writing +1 */
    int MAX_AGE = 123;
    int MIN_AGE = 0;
    String DEFAULT_MESSAGE = "Given date outside of age range " + MIN_AGE + " and " + MAX_AGE + " years.";

    String message() default DEFAULT_MESSAGE;


    int maxYears() default MAX_AGE;
    int minYears() default MIN_AGE;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
