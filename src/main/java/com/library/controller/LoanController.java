package com.library.controller;

import com.library.entity.Loan;
import com.library.service.LoanService;
import com.library.utils.dto.Loan.CreateLoanDto;
import com.library.utils.dto.Loan.SearchLoanDto;
import com.library.utils.dto.Loan.UpdateLoanDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.payload.ResponseMessage;
import com.library.utils.projections.LoanView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @GetMapping("/{id}")
    public LoanView getLoan(@PathVariable("id") final Long loanId) {
        return this.loanService.findOne(loanId);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getLoans(
            @Valid SearchLoanDto searchParams,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return this.loanService.findAll(searchParams, page, pageSize);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<Loan>> createLoan(@RequestBody @Valid CreateLoanDto params) {
        return this.loanService.create(params);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<ResponseBody>>
    updateLoan(@PathVariable("id") final Long loanId,
               @RequestBody @Valid UpdateLoanDto updateParams) {
        return this.loanService.update(loanId, updateParams);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<ResponseBody>>
    deleteLoan(@PathVariable("id") final Long loanId) {
        return this.loanService.delete(loanId);
    }
}
