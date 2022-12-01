package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "author")
public class Author {
    @JsonIgnore
    @Id
    @SequenceGenerator(name = "author_id", sequenceName = "author_id", allocationSize = 1,initialValue = 4)
    @GeneratedValue(generator = "author_id", strategy = SEQUENCE)
    private Long authorId;

    @Column(length = 20)
    private String authorFirstName;

    @Column(length = 25)
    private String authorLastName;

    @JsonBackReference(value = "book-authors")
    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Book> books;
}
