package com.library.utils.annotations;

import com.library.utils.annotations.classes.MinDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MinDateValidator.class )
public @interface MinDate {
    String message() default "Date cannot be less than current date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
