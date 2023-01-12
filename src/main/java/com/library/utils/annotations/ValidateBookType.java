package com.library.utils.annotations;

import com.library.utils.annotations.classes.BookTypeValidator;

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
@Constraint(validatedBy = BookTypeValidator.class )
public @interface ValidateBookType {
    String type();

    String message() default "Please provide one of the following values: " +
            "book,script,collection,graduation thesis,magazines,article," +
            "master thesis,doctoral thesis";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
