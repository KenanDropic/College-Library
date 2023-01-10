package com.library.service;

import com.library.entity.Book;
import com.library.entity.BookMeta;
import com.library.entity.BookWriteOff;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.BookMetaRepository;
import com.library.repository.BookRepository;
import com.library.repository.BookWriteOffRepository;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Book.CreateBookDto;
import com.library.utils.dto.Book.SearchBookDto;
import com.library.utils.dto.Book.UpdateBookDto;
import com.library.utils.dto.Book.WriteOffDto;
import com.library.utils.mapper.UpdateBookMapper;
import com.library.utils.mapper.UpdateBookMetaMapper;
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
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class BooksService {

    private final BookRepository bookRepository;
    private final BookMetaRepository bookMetaRepository;
    private final BookWriteOffRepository bookWriteOffRepository;
    private final UpdateBookMapper updateBookMapper;
    private final UpdateBookMetaMapper updateBookMetaMapper;

    public BooksService(BookRepository bookRepository, BookMetaRepository bookMetaRepository,
                        BookWriteOffRepository bookWriteOffRepository,
                        UpdateBookMapper updateBookMapper,
                        UpdateBookMetaMapper updateBookMetaMapper) {
        this.bookRepository = bookRepository;
        this.bookMetaRepository = bookMetaRepository;
        this.bookWriteOffRepository = bookWriteOffRepository;
        this.updateBookMapper = updateBookMapper;
        this.updateBookMetaMapper = updateBookMetaMapper;
    }


    public Book findOne(Long bookId) {
        return this.bookRepository.findByBookId(bookId);
    }

    public ResponseEntity<PaginationResponse> findAll(SearchBookDto searchParams,
                                                      Integer page, Integer pageSize) {

        SortingPagination.containsDirection(searchParams.getDirection());
        SortingPagination.containsField(List.of("source_title", "release_year"), searchParams.getField());

        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("source_title") ?
                        "b.source_title" :
                        "b.release_year").ascending() :
                Sort.by(Objects.equals(searchParams.getField(), "source_title") ?
                        "b.source_title" :
                        "b.release_year").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<BooksView> books = this.bookRepository.findAllBooks(searchParams, paging);

        if (books.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, books.getTotalPages(),
                            page, books.getContent()));
        }

        SortingPagination.doesHaveNext(books, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, books.getSize(), books.getTotalElements(),
                        books.getTotalPages(), page, SortingPagination.getPagination(), books.getContent()));

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

    public ResponseEntity<ResponseMessage<Book>> update(Long bookId, UpdateBookDto updateParams) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book " + bookId + " not found"));

        BookMeta bookMeta = this.bookMetaRepository
                .findMetaByBookId(bookId)
                .orElseThrow(() -> new NotFoundException("Book meta not found for book " + bookId));

        this.updateBookMapper.updateEntityFromDto(updateParams, book);
        this.updateBookMetaMapper.updateEntityFromDto(updateParams.getMeta_data(), bookMeta);
        this.bookRepository.save(book);
        this.bookMetaRepository.save(bookMeta);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Book " + bookId + " updated successfully",
                        book));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> delete(Long bookId) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book " + bookId + " not found."));

        this.bookRepository.delete(book);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Book " + bookId + " deleted successfully"));
    }

    public Book writeOff(Long bookId, WriteOffDto writeOff) {
        Book book = this.bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book " + bookId + " not found."));

        BookWriteOff bookWriteOff = new BookWriteOff(book, writeOff.getWriteOffReason(),
                writeOff.getWriteOffYear(), writeOff.getWriteOffDocument(), writeOff.getWriteOffNote());

        book.setStatus("IN");

        this.bookWriteOffRepository.save(bookWriteOff);
        return this.bookRepository.save(book);
    }

}
