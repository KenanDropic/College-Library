package com.library.repository;

import com.library.entity.User;
import com.library.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepository extends
        JpaRepository<VerificationToken, Long> {

    String updateQuery = "update VerificationToken vt set vt.confirmEmailToken = :token " +
            "where vt.user.id = :userId";
    String matchingDeleteQuery = "delete from VerificationToken vt where vt.user.id = :id";
    String expiredDeleteQuery = "delete from VerificationToken t where t.expiresAt <= ?1";

    VerificationToken findByConfirmEmailToken(String token);

    VerificationToken findByUser(User user);

    @Modifying
    @Query(value = updateQuery)
    void updateVerificationToken(@Param("token") String token, @Param("userId") Long id);

    @Modifying
    @Query(value = matchingDeleteQuery)
    void deleteByUserId(@Param("id") Long userId);

    @Modifying
    @Query(value = expiredDeleteQuery)
    void deleteAllExpiredSince(LocalDateTime now);

}
