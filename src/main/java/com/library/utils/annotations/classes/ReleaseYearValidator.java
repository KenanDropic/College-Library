package com.library.utils.annotations.classes;


import com.library.utils.annotations.ValidateReleaseYear;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseYearValidator implements ConstraintValidator<ValidateReleaseYear, Integer> {
    @SneakyThrows
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("Release year validation value: " + value);
        if (value == null && ValidateReleaseYear.class.getMethod("type").toString().equals("update")) {
            return true;
        }

        return value > 1500 && value < LocalDate.now().getYear();
    }
}
