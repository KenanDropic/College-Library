package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookMeta {
    @Id
    @SequenceGenerator(name = "meta_id", sequenceName = "meta_id", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "meta_id", strategy = SEQUENCE)
    private Long bookMetaId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference(value = "book-meta")
    private Book book;

    private String bookType;

    @Column(columnDefinition = "smallint")
    private Integer pageNumbers;

    @Column(length = 15)
    private String binding;

    @Column(length = 3)
    private String size;

    @Column(length = 5)
    private String shape;

    @Column(length = 4)
    private String eForm = null;

    @Column(length = 50)
    private String eLocation = null;
}
