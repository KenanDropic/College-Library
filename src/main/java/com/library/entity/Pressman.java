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
public class Pressman {

    public Pressman(String pressmanName) {
        this.pressmanName = pressmanName;
    }

    @Id
    @SequenceGenerator(name = "pressman_id", sequenceName = "pressman_id", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "pressman_id", strategy = SEQUENCE)
    private Long pressmanId;

    @Column(length = 45)
    private String pressmanName;

    @OneToMany(mappedBy = "pressman", cascade = {MERGE, DETACH, REFRESH, PERSIST})
    @JsonIgnore
    private List<Book> books;
}
