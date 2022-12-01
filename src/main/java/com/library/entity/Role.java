package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Table(name = "roles")
public class Role {

    @Id
    @SequenceGenerator(name = "role_id", sequenceName = "role_id", allocationSize = 1)
    @GeneratedValue(generator = "role_id", strategy = SEQUENCE)
    private Long id;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
    private String name;

    public Role() {
        super();
    }

    public Role(String name) {
        super();
        this.name = name;
    }

}

