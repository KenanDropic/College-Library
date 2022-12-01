package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "co_author")
public class CoAuthor {
    @Id
    @SequenceGenerator(name = "co_author_id", sequenceName = "co_author_id", allocationSize = 1,initialValue = 3)
    @GeneratedValue(generator = "co_author_id", strategy = SEQUENCE)
    private Long coAuthorId;

    @Column(length = 20)
    private String coAuthorFirstName;

    @Column(length = 25)
    private String coAuthorLastName;

    @JsonBackReference(value = "book-co_authors")
    @ManyToMany
    @JoinTable(name = "book_co_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "co_author_id"))
    private List<Book> booksCo;
}
