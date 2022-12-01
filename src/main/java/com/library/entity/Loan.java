package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Loan {

    public Loan(User user, Book book, boolean returnObligation,
                LocalDate borrowDate, LocalDate dueDate, LocalDate returnedDate,
                boolean loanExtended, String loanStatus) {
        this.user = user;
        this.book = book;
        this.returnObligation = returnObligation;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.loanExtended = loanExtended;
        this.loanStatus = loanStatus;
    }

    @Id
    @SequenceGenerator(name = "loan_id", sequenceName = "loan_id", allocationSize = 1)
    @GeneratedValue(generator = "loan_id", strategy = SEQUENCE)
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-loans")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference(value = "book-loans")
    private Book book;

    private boolean returnObligation;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private boolean loanExtended;

    @Column(columnDefinition = "character varying(1)")
    private String loanStatus;

    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

}
