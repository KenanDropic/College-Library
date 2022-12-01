package com.library.service;

import com.library.entity.Book;
import com.library.entity.BookWriteOff;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.BookMetaRepository;
import com.library.repository.BookRepository;
import com.library.repository.BookWriteOffRepository;
import com.library.utils.Pagination;
import com.library.utils.dto.Book.CreateBookDto;
import com.library.utils.dto.Book.SearchBookDto;
import com.library.utils.dto.Book.UpdateBookDto;
import com.library.utils.dto.Book.WriteOffDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.payload.ResponseMessage;
import com.library.utils.projections.BooksView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class BooksService {

    private final BookRepository bookRepository;
    private final BookMetaRepository bookMetaRepository;
    private final BookWriteOffRepository bookWriteOffRepository;

    public BooksService(BookRepository bookRepository,
                        BookMetaRepository bookMetaRepository,
                        BookWriteOffRepository bookWriteOffRepository) {
        this.bookRepository = bookRepository;
        this.bookMetaRepository = bookMetaRepository;
        this.bookWriteOffRepository = bookWriteOffRepository;
    }


    public Book findOne(Long bookId) {
        return this.bookRepository.findByBookId(bookId);
    }

    public ResponseEntity<PaginationResponse> findAll(SearchBookDto searchParams,
                                                      Integer page, Integer pageSize) {

        assert searchParams.getDirection() != null;
        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("source_title") ?
                        "b.source_title" :
                        "b.release_year").ascending() :
                Sort.by(Objects.equals(searchParams.getField(), "source_title") ?
                        "b.source_title" :
                        "b.release_year").descending();

        Pageable paging = page == 1 ? PageRequest.of(0, pageSize, sort) :
                PageRequest.of(page - 1, pageSize);

        Page<BooksView> books = this.bookRepository.findAllBooks(searchParams, paging);

        if (books.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, books.getTotalPages(),
                            page, books.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(books, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, books.getSize(), books.getTotalElements(),
                        books.getTotalPages(), page, pagination.getPagination(), books.getContent()));

    }

    public Book createBook(CreateBookDto bookParams) {

        Book book = new Book(bookParams.getSourceTitle(), bookParams.getSourceSubtitle(),
                bookParams.getBosnianTitle(), bookParams.getBosnianSubtitle(),
                bookParams.getPublicationOrdinalNumber(), bookParams.getAuthors(),
                bookParams.getCo_authors(), bookParams.getLanguage(), bookParams.getPublisher(),
                bookParams.getPressman(), bookParams.getReleaseYear(), bookParams.getCip_carrier(),
                bookParams.getCipNumber(), bookParams.getIsbn(), bookParams.getCOBISS(),
                bookParams.getInStock(), bookParams.getNote(),
                bookParams.getMeta_data(), bookParams.getBookProcurements());

        return this.bookRepository.save(book);
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> update(Long bookId, UpdateBookDto updateParams) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with the given ID!"));

        this.bookRepository.updateBook(book.getBookId(), updateParams);
        this.bookMetaRepository.updateBookMeta(book.getBookId(), updateParams.getMeta_data());

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Book updated successfully"));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> delete(Long bookId) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Loan not found with the given ID!"));

        this.bookRepository.delete(book);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, "Book deleted successfully"));
    }

    public Book writeOff(Long bookId, WriteOffDto writeOff) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found,with the given ID!"));

        BookWriteOff bookWriteOff = new BookWriteOff(book, writeOff.getWriteOffReason(),
                writeOff.getWriteOffYear(), writeOff.getWriteOffDocument(), writeOff.getWriteOffNote());

        book.setStatus("IN");

        this.bookWriteOffRepository.save(bookWriteOff);
        return this.bookRepository.save(book);
    }

}
