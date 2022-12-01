package com.library.utils.annotations;

import com.library.utils.annotations.classes.LanguageValidator;

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
@Constraint(validatedBy = LanguageValidator.class)
public @interface Language {

    String type();

    String message() default "Please provide one of the following values: " +
            "AL(Albanian),EN(English),DE(German),BA(Bosnian),PT(Portuguese),HR(Croatian)" +
            "FR(French),IT(Italian),MK(Macedonian),RU(Russian),SR(Serbian),SL(Slovenian)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}