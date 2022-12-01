package com.library.repository;

import com.library.entity.ResetPasswordToken;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ResetPasswordTokenRepository extends
        JpaRepository<ResetPasswordToken, Long> {

    String updateQuery = "update ResetPasswordToken t set t.resetPasswordToken = :token " +
            " where t.user.id = :userId";
    String matchingDeleteQuery = "delete from ResetPasswordToken t where t.user.id = :id";
    String expiredDeleteQuery = "delete from ResetPasswordToken t where t.expiresAt <= ?1";

    ResetPasswordToken findByResetPasswordToken(String token);

    ResetPasswordToken findByUser(User user);

    @Modifying
    @Query(value = updateQuery)
    void updateResetPasswordToken(@Param("token") String token, @Param("userId") Long id);

    @Modifying
    @Query(value = matchingDeleteQuery)
    void deleteByUserId(@Param("id") Long userId);

    @Modifying
    @Query(value = expiredDeleteQuery)
    void deleteAllExpiredSince(LocalDateTime now);
}
