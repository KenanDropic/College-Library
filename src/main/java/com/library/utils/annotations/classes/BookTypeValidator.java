package com.library.utils.annotations.classes;

import com.library.utils.annotations.BookType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class BookTypeValidator implements ConstraintValidator<BookType, String> {

    @Override
    public boolean isValid(String bookType, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("Book type:" + bookType);

        List<String> bookTypes = Arrays
                .asList("book", "script", "collection", "graduation thesis",
                        "magazines", "article", "master thesis", "doctoral thesis");
        return bookTypes.contains(bookType);
    }
}
