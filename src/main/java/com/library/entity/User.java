package com.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener.class)
public class User {
    public Set<Role> getRoles() {
        return this.roles;
    }

    public User(String first_name, String last_name, String email,
                String password, String phone) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @Id
    @SequenceGenerator(name = "user_id", sequenceName = "user_id", allocationSize = 1, initialValue = 4)
    @GeneratedValue(generator = "user_id", strategy = SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @Column(unique = true)
    @NotNull
    private String email;
    private Boolean emailConfirmed = false;
    @JsonIgnore
    @NotNull
    private String password;

    @JsonIgnore
    private String hashedRt = null;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = {MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    //private ArrayList<String> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ResetPasswordToken resetPasswordToken;

    @NotNull
    private String phone;
    @Nullable
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-loans")
    private List<Loan> loans;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    public void addRole(Role role) {
        this.getRoles().add(role);
    }

}
