package com.library.utils.annotations;

import com.library.utils.annotations.classes.ProcurementWayValidator;

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
@Constraint(validatedBy = ProcurementWayValidator.class)
public @interface ProcurementWay {
    String message() default "Please provide one of the following values: " +
            "donation,loan,purchase,other";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
