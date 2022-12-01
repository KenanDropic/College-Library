package com.library.service;

import com.library.entity.Book;
import com.library.entity.Loan;
import com.library.entity.User;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.UserRepository;
import com.library.utils.Pagination;
import com.library.utils.dto.Loan.CreateLoanDto;
import com.library.utils.dto.Loan.SearchLoanDto;
import com.library.utils.dto.Loan.UpdateLoanDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.payload.ResponseMessage;
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

    public LoanService(LoanRepository loanRepository, UserRepository userRepository,
                       BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }
    /*Because you are using the @JsonBackReference on the Customer property in the Loan entity,
    the Customer object will not included in the serialization. Use the @JsonManagedReference for
    the Customer in the Loan object and use @JsonBackReference on the Loan property in the Customer entity.
    This will serialize the Customer property of your Loan entity. But the Customer object serialization
    will not contains the Loan property. You need to pick one side of the relationship to serialize.
    To allow both side, use @JsonIdentityInfo annotation in your entity and remove the @JsonBackReference and
    @JsonManagedReference. You entities will be something like:*/
    public Loan findOne(Long loanId) {
        return this.loanRepository
                .findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with the given ID!"));
    }

    public ResponseEntity<PaginationResponse> findAll(SearchLoanDto searchParams,
                                                      Integer page, Integer pageSize) {

        List<String> fields = Arrays.asList("source_title", "borrow_date", "created_at");
        List<String> directions = Arrays.asList("ASC", "DESC");

        if (!fields.contains(searchParams.getField())) {
            throw new BadRequestException(
                    "Sorting is allowed only by three fields: source_title, borrow_date and created_at");
        }

        if (!directions.contains(searchParams.getDirection())) {
            throw new BadRequestException("Sorting is possible only by two directions: ASC or DESC");
        }


        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<LoansView> loans = this.loanRepository.findAllLoans(searchParams, paging);

        if (loans.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, loans.getTotalPages(),
                            page, loans.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(loans, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, loans.getSize(), loans.getTotalElements(),
                        loans.getTotalPages(), page, pagination.getPagination(), loans.getContent()));
    }

    public ResponseEntity<ResponseMessage<Loan>> create(CreateLoanDto params) {
        User user = this.userRepository
                .findById(params.getUser_id())
                .orElseThrow(() -> new NotFoundException("Please provide existing user id"));

        Book book = this.bookRepository
                .findById(params.getBook_id())
                .orElseThrow(() -> new NotFoundException("Please provide existing book id"));

        if (book.getInStock() < 1) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(false, "This book is out stock,we cannot loan it."));
        }

        checkDates(params.getReturnedDate(), params.getDueDate(), params.getBorrowDate());

        Loan loan = new Loan(user, book, params.isReturnObligation(),
                params.getBorrowDate(), params.getDueDate(), params.getReturnedDate(),
                params.isLoanExtended(), params.getLoanStatus());

        // subtract stock by 1
        this.bookRepository.updateBookStock(book.getBookId());

        return ResponseEntity
                .status(201)
                .body(new ResponseMessage<>(true,
                        "Loan created successfully",
                        this.loanRepository.save(loan)));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> update(Long loanId, UpdateLoanDto updateParams) {
        Loan loan = this.loanRepository
                .findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with the given ID!"));

        checkDates(updateParams.getReturnedDate(), updateParams.getDueDate(), loan.getBorrowDate());

        this.loanRepository.updateLoan(loanId, updateParams);
        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Loan updated successfully"));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> delete(Long loanId) {
        Loan loan = this.loanRepository
                .findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with the given ID!"));

        this.loanRepository.delete(loan);

        return ResponseEntity.status(200).body(new ResponseMessage<>(true, "Loan deleted successfully"));
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
