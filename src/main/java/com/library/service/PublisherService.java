package com.library.service;

import com.library.entity.Publisher;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.PublisherRepository;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Publisher.PublisherDto;
import com.library.utils.dto.Publisher.SearchPublisherDto;
import com.library.utils.payload.PaginationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public ResponseEntity<PaginationResponse> findAllPublishers(
            SearchPublisherDto params, Integer page, Integer pageSize) {

        SortingPagination.containsDirection(params.getDirection());
        SortingPagination.containsField(List.of("publisher_id", "publisher_name"), params.getField());

        Sort sort = params.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(params.getField()).equals("publisher_id") ?
                        "publisher_id" :
                        "publisher_name").ascending() :
                Sort.by(Objects.equals(params.getField(), "source_title") ?
                        "publisher_id" :
                        "publisher_name").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<Publisher> publishers = this.publisherRepository.findPublishers(params, paging);

        if (publishers.isEmpty()) {
            throw new BadRequestException("Publishers are not found.");
        }

        SortingPagination.doesHaveNext(publishers, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, publishers.getSize(),
                        publishers.getTotalElements(), publishers.getTotalPages(),
                        page, SortingPagination.getPagination(), publishers.getContent()));


    }

    public Publisher createPublisher(PublisherDto params) {
        Publisher doesPublisherExists = this
                .publisherRepository
                .findByPublisherName(params
                        .getPublisherName()
                        .toLowerCase()
                        .replaceAll("\\s", ""))
                .orElseThrow(() -> new NotFoundException("Publisher " +
                        params.getPublisherName() +
                        " not found!"));

        // if not null,record of Publisher is returned,so it means it exists.
        if (doesPublisherExists != null) {
            throw new BadRequestException("Publisher " +
                    params.getPublisherName() +
                    " already exists!");
        }

        Publisher publisher = new Publisher(params.getPublisherName());

        return this.publisherRepository.save(publisher);
    }
}

