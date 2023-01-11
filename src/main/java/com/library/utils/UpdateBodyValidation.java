package com.library.utils;

import com.library.exception.exceptions.BadRequestException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UpdateBodyValidation<T> {

    public void checkRequestBody(List<T> existingValues, List<Object> passedValues) {
        List<Boolean> equality = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        boolean areAllNull = passedValues.stream().allMatch(Objects::isNull);

        if (areAllNull) {
            throw new BadRequestException("Please provide values for update. Object cannot be empty!");
        }

        for (int i = 0, n = existingValues.size(); i < n; i++) {
            if (passedValues.get(i) == null) {
                equality.add(false);
            } else if (passedValues.get(i).equals(existingValues.get(i))) {
                equality.add(passedValues.get(i).equals(existingValues.get(i)));
                values.add(existingValues.get(i));
            } else {
                equality.add(false);
            }
        }

        if (equality.contains(true)) {
            String verb = values.size() <= 1 ? " is" : " are";
            String noun = values.size() <= 1 ? "one" : "ones";
            throw new BadRequestException("When updating please provide new values. " +
                    values + verb + " same as the " + noun + " in database");
        }
    }
}
