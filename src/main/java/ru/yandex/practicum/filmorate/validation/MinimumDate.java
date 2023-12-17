package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.constraints.Past;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Past
public @interface MinimumDate {
    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}

