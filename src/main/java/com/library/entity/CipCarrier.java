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
public class CipCarrier {
    @Id
    @SequenceGenerator(name = "cip_carrier_id", sequenceName = "cip_carrier_id", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "cip_carrier_id", strategy = SEQUENCE)
    private Long cipCarrierId;

    @Column(length = 45)
    private String cipName;

    @OneToMany(mappedBy = "cipCarrier", cascade = {MERGE, DETACH, PERSIST, REFRESH})
    @JsonIgnore
    List<Book> books;
}

