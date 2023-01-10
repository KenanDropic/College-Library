package com.library.repository;

import com.library.entity.Loan;
import com.library.utils.dto.Loan.SearchLoanDto;
import com.library.utils.dto.Loan.UpdateLoanDto;
import com.library.utils.projections.LoanView;
import com.library.utils.projections.LoansView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = """
            SELECT  l.loan_id,l.borrow_date,l.due_date,l.loan_status,
                    b.*,u.*
                    FROM loan l
                    INNER JOIN  (SELECT book_id,source_title
                                FROM book WHERE :#{#searchParams.title} is null\s
                                OR source_title = cast(:#{#searchParams.title} as character varying)
                                ) b ON b.book_id = l.book_id
                    INNER JOIN (SELECT user_id,concat(first_name,' ',last_name) as fullname
                                FROM "user" WHERE :#{#searchParams.member} is null\s
                                OR cast(:#{#searchParams.member} as character varying) = first_name
                                OR cast(:#{#searchParams.member} as character varying) = last_name OR\s
                                cast(:#{#searchParams.member} as character varying) =
                                cast(concat(first_name,' ',last_name) as character varying)
                                ) u ON u.user_id = l.user_id
                    WHERE (cast(:#{#searchParams.fromDate} as character varying) is null OR
                    cast(l.borrow_date as character varying)  BETWEEN SYMMETRIC cast(:#{#searchParams.fromDate} as character varying)
                    AND cast(:#{#searchParams.toDate} as character varying)) AND
                    (:#{#searchParams.status} is null OR cast(:#{#searchParams.status} as character varying) = l.loan_status)
                    """, nativeQuery = true)
    Page<LoansView> findAllLoans(SearchLoanDto searchParams, Pageable pageable);

    /*
    ORDER BY
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'source_title' AND
                    cast(:#{#searchParams.direction} as character varying) = 'ASC' THEN b.source_title END,
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'source_title' AND
                    cast(:#{#searchParams.direction} as character varying) = 'DESC' THEN b.source_title END DESC,
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'borrow_date' AND
                    cast(:#{#searchParams.direction} as character varying) = 'ASC' THEN l.borrow_date END,
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'borrow_date' AND
                    cast(:#{#searchParams.direction} as character varying) = 'DESC' THEN l.borrow_date END DESC,
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'created_at' AND
                    cast(:#{#searchParams.direction} as character varying) = 'ASC' THEN l.created_at END,
                    CASE WHEN cast(:#{#searchParams.field} as character varying) = 'created_at' AND
                    cast(:#{#searchParams.direction} as character varying) = 'DESC' THEN l.created_at END DESC
     */

    @Modifying
    @Query(value = """
            UPDATE loan l SET
            user_id = coalesce(cast(cast(:#{#updateParams.userId} as text) as bigint),l.user_id),
            due_date = coalesce(cast(cast(:#{#updateParams.dueDate} as text) as date),l.due_date),
            returned_date = coalesce(cast(cast(:#{#updateParams.returnedDate} as text) as date),l.returned_date),
            loan_extended = coalesce(cast(cast(:#{#updateParams.loanExtended} as text) as boolean),l.loan_extended),
            loan_status = coalesce(cast(cast(:#{#updateParams.loanStatus} as text) as character varying),l.loan_status)
            WHERE loan_id = :id
            """, nativeQuery = true)
    void updateLoan(@Param(value = "id") Long id, UpdateLoanDto updateParams);

    @Query(value = """
            SELECT l.loan_id,l.borrow_date,l.due_date,l.loan_status,
                   b.book_id,b.source_title,b.release_year,b.language,
                   u.user_id,concat(u.first_name,' ',u.last_name) as fullname,u.email
                   FROM loan l
                   INNER JOIN book b on b.book_id = l.book_id
                   INNER JOIN "user" u on u.user_id = l.user_id
                   WHERE loan_id = :loanId""", nativeQuery = true)
    Optional<LoanView> findLoan(@Param("loanId") Long loanId);
}
