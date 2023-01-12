package com.library.utils.annotations.classes;

import com.library.utils.annotations.ValidateWriteOffOptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class BookWriteOffValidator implements ConstraintValidator<ValidateWriteOffOptions, String> {

    @Override
    public boolean isValid(String option, ConstraintValidatorContext constraintValidatorContext) {
        List<String> writeOffOptions = Arrays.asList("destruction", "donation",
                "return", "sale", "expiration date");

        return writeOffOptions.contains(option);
    }
}
