package com.library.utils.annotations.classes;

import com.library.utils.annotations.Status;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StatusValidator implements ConstraintValidator<Status, String> {
    @SneakyThrows
    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        List<String> statusTypes = Arrays.asList("AC", "NA", "UK");

        if (Status.class.getMethod("type").toString().equals("create")) {
            return statusTypes.contains(status);
        }


        return (statusTypes.contains(status) || status == null);
    }
}
