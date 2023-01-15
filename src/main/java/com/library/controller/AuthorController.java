package com.library.controller;

import com.library.entity.Author;
import com.library.service.AuthorService;
import com.library.utils.dto.Author.CreateAuthorDto;
import com.library.utils.dto.Author.SearchAuthorDto;
import com.library.utils.payload.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    // on front-end start searching when text length is gte of 3 chars
    @GetMapping
    public ResponseEntity<PaginationResponse> getAuthors(
            @Valid SearchAuthorDto params,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return this.authorService.findAllAuthors(params, page, pageSize);
    }


    @PostMapping
    public Author createAuthor(@RequestBody @Valid CreateAuthorDto params) {
         return this.authorService.createAuthor(params);
    }
}
