package com.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Publisher {

    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }

    @Id
    @SequenceGenerator(name = "publisher_id", sequenceName = "publisher_id", allocationSize = 1, initialValue = 4)
    @GeneratedValue(generator = "publisher_id", strategy = SEQUENCE)
    private Long publisherId;

    @Column(length = 30)
    private String publisherName;

    @OneToMany(mappedBy = "publisher", cascade = {MERGE, DETACH, PERSIST, REFRESH})
    @JsonIgnore
    private List<Book> books;
}
