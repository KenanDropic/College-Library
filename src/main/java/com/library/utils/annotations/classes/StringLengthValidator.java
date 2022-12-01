package com.library.utils.annotations.classes;

import com.library.utils.annotations.ValidateStringLength;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringLengthValidator implements ConstraintValidator<ValidateStringLength, String> {
    @SneakyThrows
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return String.valueOf(value.length()).equals(ValidateStringLength.class.getMethod("length").toString());
    }
}
