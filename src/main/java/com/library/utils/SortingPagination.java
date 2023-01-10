package com.library.utils;

import com.library.exception.exceptions.BadRequestException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class SortingPagination {
    static HashMap<String, Integer> pagination = new HashMap<>();
    static List<String> directions = Arrays.asList("ASC","DESC");

    public static HashMap<String, Integer> getPagination() {
        return pagination;
    }

    public static <T> void doesHaveNext(Page<T> items, Integer page) {
        if (items.hasNext()) {
            pagination.put("next", page + 1);
        } else if (items.hasPrevious()) {
            pagination.put("previous", page - 1);
        }
    }

    public static void containsField(List<String> fieldList, String field) {
        if (!fieldList.contains(field)) {
            throw new BadRequestException("Sorting is allowed only by fields: " + fieldList);
        }
    }

    public static void containsDirection(String direction) {
        if (!directions.contains(direction)) {
            throw new BadRequestException("Sorting is possible only by two directions: ASC or DESC");
        }
    }
}
