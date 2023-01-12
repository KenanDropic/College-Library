package com.library.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book {

    public Book(String sourceTitle, String sourceSubtitle, String bosnianTitle
            , String bosnianSubtitle, Integer publicationOrdinalNumber,
                List<Author> authors, List<CoAuthor> co_authors, String language,
                Publisher publisher, Pressman pressman, Integer releaseYear,
                CipCarrier cip_carrier, String cipNumber, String isbn, String cobiss,
                Integer inStock, String note, List<BookMeta> meta_data,
                List<BookProcurement> bookProcurements) {
        this.sourceTitle = sourceTitle;
        this.sourceSubtitle = sourceSubtitle;
        this.bosnianTitle = bosnianTitle;
        this.bosnianSubtitle = bosnianSubtitle;
        this.publicationOrdinalNumber = publicationOrdinalNumber;
        this.authors = authors;
        this.coAuthors = co_authors;
        this.language = language;
        this.publisher = publisher;
        this.pressman = pressman;
        this.inStock = inStock;
        this.note = note;
        this.releaseYear = releaseYear;
        this.cipCarrier = cip_carrier;
        this.cipNumber = cipNumber;
        this.isbn = isbn;
        this.cobiss = cobiss;
        this.metadata = meta_data;
        this.bookProcurements = bookProcurements;
    }

    @Id
    @SequenceGenerator(name = "book_id", sequenceName = "book_id", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "book_id", strategy = SEQUENCE)
    private Long bookId;

    @Column(length = 45)
    private String sourceTitle;

    @Column(length = 45)
    private String sourceSubtitle;

    @Column(length = 45)
    private String bosnianTitle;

    @Column(length = 45)
    private String bosnianSubtitle;

    @Column(length = 3, columnDefinition = "smallint")
    private Integer publicationOrdinalNumber;

    @JsonManagedReference(value = "book-authors")
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER,
            cascade = {PERSIST, MERGE, DETACH, REFRESH})
    private List<Author> authors;

    @JsonManagedReference(value = "book-co_authors")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "booksCo",
            cascade = {PERSIST, MERGE, DETACH, REFRESH})
    private List<CoAuthor> coAuthors;

    @Column(length = 2)
    private String language;

    @ManyToOne
    @JoinColumn(name = "publisherId")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "pressmanId")
    private Pressman pressman;

    @Column(length = 4)
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "cipCarrierId")
    private CipCarrier cipCarrier;

    @Column(length = 15)
    private String cipNumber;

    @Column(length = 18)
    private String isbn;

    @Column(length = 25)
    private String cobiss;

    @Column(length = 2)
    private String status;

    private Integer inStock;

    @Column(length = 100)
    private String note;

    @OneToMany(mappedBy = "book", cascade = ALL)
    @JsonManagedReference(value = "book-meta")
    List<BookMeta> metadata;

    @OneToMany(mappedBy = "book", cascade = ALL)
    @JsonManagedReference(value = "procurements")
    List<BookProcurement> bookProcurements;

    @OneToMany(mappedBy = "book", cascade = {MERGE, DETACH, REFRESH, PERSIST})
    @JsonManagedReference(value = "book-loans")
    List<Loan> loans;

    @JsonManagedReference
    @OneToOne(mappedBy = "book", cascade = {MERGE, PERSIST, REFRESH, DETACH})
    private BookWriteOff bookWriteOff;

    private Date createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = new Date();
    }


}
