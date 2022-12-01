package com.library.utils.annotations.classes;

import com.library.utils.annotations.Language;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class LanguageValidator implements ConstraintValidator<Language, String> {
    @SneakyThrows
    @Override
    public boolean isValid(String language, ConstraintValidatorContext constraintValidatorContext) {
        List<String> languages = Arrays
                .asList("AL", "EN", "DE", "BA", "PT", "HR", "FR", "IT", "MK", "RU", "SR", "SL");

        if (Language.class.getMethod("type").toString().equals("create")) {
            return languages.contains(language);
        }

        return (languages.contains(language) || language == null);
    }
}
