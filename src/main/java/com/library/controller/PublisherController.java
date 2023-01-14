package com.library.controller;

import com.library.entity.Publisher;
import com.library.service.PublisherService;
import com.library.utils.dto.Publisher.PublisherDto;
import com.library.utils.dto.Publisher.SearchPublisherDto;
import com.library.utils.payload.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/publisher")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    // on front-end start searching when text length is gte of 3 chars
    @GetMapping
    public ResponseEntity<PaginationResponse> getPublishers(
            @Valid SearchPublisherDto params,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return this.publisherService.findAllPublishers(params, page, pageSize);
    }


    @PostMapping
    public Publisher createPublisher(@RequestBody @Valid PublisherDto params) {
        return this.publisherService.createPublisher(params);
    }

}
