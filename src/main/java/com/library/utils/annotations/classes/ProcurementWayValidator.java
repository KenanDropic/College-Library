package com.library.utils.annotations.classes;

import com.library.utils.annotations.ProcurementWay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ProcurementWayValidator implements ConstraintValidator<ProcurementWay, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        List<String> ways = Arrays.asList("donation", "loan", "purchase", "other");

        return ways.contains(value);
    }
}
