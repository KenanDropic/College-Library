package com.library.utils.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class PaginationResponse {
    private boolean success;
    private Integer count;
    private Long totalElements;
    private Integer pages;
    private Integer page;
    private HashMap<String, Integer> pagination;
    private List<?> data;

    public PaginationResponse(boolean success, Integer count, Long totalElements,
                              Integer pages, Integer page,
                              HashMap<String, Integer> pagination, List<?> data) {
        this.success = success;
        this.count = count;
        this.totalElements = totalElements;
        this.pages = pages;
        this.page = page;
        this.pagination = pagination;
        this.data = data;
    }

    public PaginationResponse(boolean success, Integer count,
                              Integer pages, Integer page, List<?> data) {
        this.success = success;
        this.count = count;
        this.pages = pages;
        this.page = page;
        this.data = data;
    }
}
