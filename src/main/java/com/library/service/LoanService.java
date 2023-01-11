package com.library.service;

import com.library.entity.Book;
import com.library.entity.Loan;
import com.library.entity.User;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.UserRepository;
import com.library.utils.SortingPagination;
import com.library.utils.UpdateBodyValidation;
import com.library.utils.dto.Loan.CreateLoanDto;
import com.library.utils.dto.Loan.SearchLoanDto;
import com.library.utils.dto.Loan.UpdateLoanDto;
import com.library.utils.mapper.UpdateLoanMapper;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.payload.ResponseMessage;
import com.library.utils.projections.LoanView;
import com.library.utils.projections.LoansView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UpdateLoanMapper updateLoanMapper;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository,
                       BookRepository bookRepository, UpdateLoanMapper updateLoanMapper) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.updateLoanMapper = updateLoanMapper;
    }

    public LoanView findOne(Long loanId) {
        return this.loanRepository
                .findLoan(loanId)
                .orElseThrow(() -> new NotFoundException("Loan " + loanId + " not found."));
    }

    public ResponseEntity<PaginationResponse> findAll(SearchLoanDto searchParams,
                                                      Integer page, Integer pageSize) {

        SortingPagination.containsDirection(searchParams.getDirection());
        SortingPagination.containsField(List.of("source_title", "borrow_date", "created_at"), searchParams.getField());

        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<LoansView> loans = this.loanRepository.findAllLoans(searchParams, paging);

        if (loans.isEmpty()) {
            throw new BadRequestException("Loans not found!");
        }

        SortingPagination.doesHaveNext(loans, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, loans.getSize(), loans.getTotalElements(),
                        loans.getTotalPages(), page, SortingPagination.getPagination(), loans.getContent()));
    }

    public ResponseEntity<ResponseMessage<Loan>> create(CreateLoanDto params) {
        User user = this.userRepository
                .findById(params.getUser_id())
                .orElseThrow(() -> new NotFoundException("User " + params.getUser_id() + " not found."));

        Book book = this.bookRepository
                .findById(params.getBook_id())
                .orElseThrow(() -> new NotFoundException("Book with " + params.getBook_id() + " not found."));

        if (book.getInStock() < 1) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(
                            false,
                            "Book " + book.getSourceTitle() + " is out of stock,we cannot loan it."));
        }

        checkDates(params.getReturnedDate(), params.getDueDate(), params.getBorrowDate());

        Loan loan = new Loan(user, book, params.isReturnObligation(),
                params.getBorrowDate(), params.getDueDate(), params.getReturnedDate(),
                params.getLoanExtended(), params.getLoanStatus());

        // subtract stock by 1
        this.bookRepository.updateBookStock(book.getBookId());

        return ResponseEntity
                .status(201)
                .body(new ResponseMessage<>(
                        true,
                        "Loan created successfully",
                        this.loanRepository.save(loan)));
    }

    public ResponseEntity<ResponseMessage<Loan>> update(Long loanId, UpdateLoanDto updateParams) {
        Loan loan = this.loanRepository
                .findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan " + loanId + " not found."));

        checkDates(updateParams.getReturnedDate(), updateParams.getDueDate(), loan.getBorrowDate());

        List<Object> existingValues = Arrays.asList(loan.getUser().getId(),
                loan.getDueDate(), loan.getLoanExtended(),
                loan.getReturnedDate(), loan.getLoanStatus());

        List<Object> passedValues = Arrays.asList(updateParams.getUserId(),
                updateParams.getDueDate(), updateParams.getLoanExtended(),
                updateParams.getReturnedDate(), updateParams.getLoanStatus());

        UpdateBodyValidation<Object> check = new UpdateBodyValidation<>();
        check.checkRequestBody(existingValues, passedValues);

        this.updateLoanMapper.updateEntityFromDto(updateParams, loan);
        this.loanRepository.save(loan);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Loan " + loanId + " updated successfully",
                        loan));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> delete(Long loanId) {
        Loan loan = this.loanRepository
                .findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan " + loanId + " not found."));

        this.loanRepository.delete(loan);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Loan " + loanId + " deleted successfully"));
    }

    /* ------------------------------------------- NON-API ------------------------------------------ */

    private void checkDates(LocalDate returnedDate, LocalDate dueDate, LocalDate borrowedDate) {
        if (returnedDate != null) {
            if (returnedDate.isBefore(borrowedDate)) {
                throw new BadRequestException("Return date cannot be before borrowed date!");
            }
        }

        if (dueDate != null) {
            if (dueDate.isBefore(borrowedDate)) {
                throw new BadRequestException("Due date cannot be before borrowed date!");
            }
        }
    }

}
