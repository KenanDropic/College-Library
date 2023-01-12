package com.library.utils.annotations;

import com.library.utils.annotations.classes.BookWriteOffValidator;

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
@Constraint(validatedBy = BookWriteOffValidator.class)
public @interface ValidateWriteOffOptions {
    String message() default "Please provide one of the following values: " +
            "destruction,donation,return,sale,expiration date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
