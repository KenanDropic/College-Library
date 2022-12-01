package com.library.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class Pagination {
    HashMap<String, Integer> pagination = new HashMap<>();

    public <T> void doesHaveNext(Page<T> items, Integer page) {
        if (items.hasNext()) {
            pagination.put("next", page + 1);
        } else if (items.hasPrevious()) {
            pagination.put("previous", page - 1);
        }

    }
}
