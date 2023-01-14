package com.library.controller;

import com.library.entity.Pressman;
import com.library.service.PressmanService;
import com.library.utils.dto.Pressman.PressmanDto;
import com.library.utils.dto.Pressman.SearchPressmanDto;
import com.library.utils.payload.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pressman")
public class PressmanController {

    private final PressmanService pressmanService;

    public PressmanController(PressmanService pressmanService) {
        this.pressmanService = pressmanService;
    }

    // on front-end start searching when text length is gte of 3 chars
    @GetMapping
    public ResponseEntity<PaginationResponse> getPressmans(
            @Valid SearchPressmanDto params,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return this.pressmanService.findAllPressmans(params, page, pageSize);
    }


    @PostMapping
    public Pressman createPressman(@RequestBody @Valid PressmanDto params) {
        return this.pressmanService.createPressman(params);
    }
}
