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
public class BookProcurement {
    @Id
    @SequenceGenerator(name = "procurement_id", sequenceName = "procurement_id", allocationSize = 1,initialValue = 5)
    @GeneratedValue(generator = "procurement_id", strategy = SEQUENCE)
    private Long bookProcurementId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference(value = "procurements")
    private Book book;

    private String procurementWay;

    @Column(length = 4)
    private Integer procurementYear;

    @Column(length = 50)
    private String procurementSource;
}
