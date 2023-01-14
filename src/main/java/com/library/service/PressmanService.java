package com.library.service;

import com.library.entity.Pressman;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.PressmanRepository;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Pressman.PressmanDto;
import com.library.utils.dto.Pressman.SearchPressmanDto;
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
public class PressmanService {

    private final PressmanRepository pressmanRepository;

    public PressmanService(PressmanRepository pressmanRepository) {
        this.pressmanRepository = pressmanRepository;
    }

    public ResponseEntity<PaginationResponse> findAllPressmans(
            SearchPressmanDto params, Integer page, Integer pageSize) {

        SortingPagination.containsDirection(params.getDirection());
        SortingPagination.containsField(List.of("pressman_id", "pressman_mame"), params.getField());

        Sort sort = params.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(params.getField()).equals("pressman_id") ?
                        "pressman_id" :
                        "pressman_name").ascending() :
                Sort.by(Objects.equals(params.getField(), "source_title") ?
                        "pressman_id" :
                        "pressman_name").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<Pressman> pressmans = this.pressmanRepository.findAllPressmans(params, paging);

        if (pressmans.isEmpty()) {
            throw new BadRequestException("Pressman's are not found.");
        }

        SortingPagination.doesHaveNext(pressmans, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, pressmans.getSize(),
                        pressmans.getTotalElements(), pressmans.getTotalPages(),
                        page, SortingPagination.getPagination(), pressmans.getContent()));


    }

    public Pressman createPressman(PressmanDto params) {
        Pressman doesPressmanExists = this
                .pressmanRepository
                .findByPressmanName(params
                        .getPressmanName()
                        .toLowerCase()
                        .replaceAll("\\s", ""))
                .orElseThrow(() -> new NotFoundException("Pressman " +
                        params.getPressmanName() +
                        " not found!"));

        // if not null,record of Pressman is returned,so it means it exists.
        if (doesPressmanExists != null) {
            throw new BadRequestException("Pressman already exists!");
        }

        Pressman pressman = new Pressman(params.getPressmanName());

        return this.pressmanRepository.save(pressman);
    }
}
