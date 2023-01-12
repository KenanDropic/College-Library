package com.library.controller;

import com.library.entity.Book;
import com.library.entity.BookWriteOff;
import com.library.service.BooksService;
import com.library.utils.dto.Book.CreateBookDto;
import com.library.utils.dto.Book.SearchBookDto;
import com.library.utils.dto.Book.UpdateBookDto;
import com.library.utils.dto.Book.WriteOffDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.payload.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BooksService booksService;

    public BookController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") final Long bookId) {
        return this.booksService.findOne(bookId);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getBooks(
            @Valid SearchBookDto searchParams,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return this.booksService.findAll(searchParams, page, pageSize);
    }


    @PostMapping
    public Book createBook(@RequestBody @Valid CreateBookDto bookParams) {
        return this.booksService.createBook(bookParams);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<Book>> updateBook(@PathVariable("id") final Long bookId,
                                                            @RequestBody @Valid UpdateBookDto updateParams) {
        return this.booksService.update(bookId, updateParams);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<ResponseBody>> deleteBook(@PathVariable("id") final Long bookId) {
        return this.booksService.delete(bookId);
    }

    @PostMapping("/writeOff/{id}")
    public ResponseEntity<ResponseMessage<BookWriteOff>> writeOffBook(@RequestBody @Valid WriteOffDto writeOff,
                                                                      @PathVariable("id") final Long bookId) {
        return this.booksService.writeOff(bookId, writeOff);
    }

}
