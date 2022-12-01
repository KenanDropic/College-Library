package com.library.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ResetPasswordToken {
    private static final int EXPIRATION = 10 * 60 * 1000;

    public ResetPasswordToken(final String resetPasswordToken) {
        super();
        this.resetPasswordToken = resetPasswordToken;
        this.expiresAt = calculateExpiryDate();
    }

    public ResetPasswordToken(final String resetPasswordToken, User user) {
        super();
        this.resetPasswordToken = resetPasswordToken;
        this.user = user;
        this.expiresAt = calculateExpiryDate();
    }

    @Id
    @SequenceGenerator(name = "email_token_id", sequenceName = "email_token_id", allocationSize = 1)
    @GeneratedValue(generator = "email_token_id", strategy = SEQUENCE)
    private Long id;

    private String resetPasswordToken;

    @OneToOne(targetEntity = User.class, fetch = EAGER, cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_RESET_PASSWORD"))
    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    private LocalDateTime calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MINUTE, ResetPasswordToken.EXPIRATION);
        return LocalDateTime.ofInstant(cal.getTime().toInstant(), ZoneId.systemDefault());
    }
}
