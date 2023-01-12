package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookWriteOff {
    public BookWriteOff(Book book, String writeOffReason, LocalDate writeOffDate,
                        String writeOffDocument, String writeOffNote) {
        this.book = book;
        this.writeOffReason = writeOffReason;
        this.writeOffDate = writeOffDate;
        this.writeOffDocument = writeOffDocument;
        this.writeOffNote = writeOffNote;
    }

    @Id
    @SequenceGenerator(name = "write_off_id", sequenceName = "write_off_id", allocationSize = 1)
    @GeneratedValue(generator = "write_off_id", strategy = SEQUENCE)
    private Long bookWriteOffId;

    @JsonBackReference
    @OneToOne(targetEntity = Book.class, fetch = FetchType.EAGER, cascade = {MERGE, PERSIST, REFRESH, DETACH})
    @JoinColumn(name = "book_id",foreignKey = @ForeignKey(name = "FK_BOOK_WRITE_OFF"))
    private Book book;

    @Column(length = 30)
    private String writeOffReason;

    @Column(length = 4)
    private LocalDate writeOffDate;

    @Column(length = 30)
    private String writeOffDocument;

    @Column(length = 100)
    private String writeOffNote;

}
