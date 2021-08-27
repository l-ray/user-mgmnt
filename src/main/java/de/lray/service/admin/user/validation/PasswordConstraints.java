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
@Constraint(validatedBy = PasswordConstraintsValidator.class)
@Documented
public @interface PasswordConstraints {

    String message() default "{message.key}";

    boolean containsDigits() default true;
    boolean containsSpecialChar() default true;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
