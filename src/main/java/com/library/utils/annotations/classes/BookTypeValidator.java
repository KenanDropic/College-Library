package com.library.utils.annotations.classes;

import com.library.utils.annotations.ValidateBookType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class BookTypeValidator implements ConstraintValidator<ValidateBookType, String> {

    @Override
    public boolean isValid(String bookType, ConstraintValidatorContext constraintValidatorContext) {
        List<String> bookTypes = Arrays
                .asList("book", "script", "collection", "graduation thesis",
                        "magazines", "article", "master thesis", "doctoral thesis");

        try {
            if (ValidateBookType.class.getMethod("type").toString().equals("create")) {
                return bookTypes.contains(bookType);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return (bookTypes.contains(bookType) || bookType == null);
    }
}
