package com.library.service;

import com.library.entity.Author;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.AuthorRepository;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Author.CreateAuthorDto;
import com.library.utils.dto.Author.SearchAuthorDto;
import com.library.utils.payload.PaginationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public ResponseEntity<PaginationResponse> findAllAuthors(SearchAuthorDto params, Integer page,
                                                             Integer pageSize) {

        SortingPagination.containsDirection(params.getDirection());
        SortingPagination.containsField(List.of("author_id", "author_name"), params.getField());

        Pageable paging = PageRequest.of(page - 1, pageSize);

        Page<Author> authors = this.authorRepository.findAuthors(params, paging);

        if (authors.isEmpty()) {
          throw new BadRequestException("Authors are not found.");
        }

        SortingPagination.doesHaveNext(authors, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, authors.getSize(),
                        authors.getTotalElements(), authors.getTotalPages(),
                        page, SortingPagination.getPagination(), authors.getContent()));
    }

    public Author createAuthor(CreateAuthorDto params) {
        Author doesAuthorExists = this
                .authorRepository
                .findByAuthorName((params.getAuthorFirstName() +
                        params.getAuthorLastName()).toLowerCase())
                .orElseThrow(() -> new NotFoundException("Author " +
                        (params.getAuthorFirstName() +
                                " " +
                                params.getAuthorLastName()) +
                        " not found!"));

        if (doesAuthorExists != null) {
            throw new BadRequestException("Author " +
                    (params.getAuthorFirstName() +
                            " " +
                            params.getAuthorLastName()) +
                    " already exists!");
        }

        Author author = new Author(params.getAuthorFirstName().trim(), params.getAuthorLastName().trim());

        return this.authorRepository.save(author);
    }
}
