package com.library.service;

import com.library.entity.ResetPasswordToken;
import com.library.entity.User;
import com.library.repository.ResetPasswordTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository,
                                     PasswordEncoder passwordEncoder) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResetPasswordToken getResetPasswordToken(final String token) {
        return resetPasswordTokenRepository.findByResetPasswordToken(token);
    }

    public void createResetPasswordToken(User user, final String token) {
        ResetPasswordToken reset_token = new ResetPasswordToken(token, user);
        resetPasswordTokenRepository.save(reset_token);
    }

    public void updateUserResetPasswordToken(String token, Long userId) {
        resetPasswordTokenRepository.updateResetPasswordToken(token, userId);
    }

    public void deleteResetPasswordToken(User user) {
        resetPasswordTokenRepository.deleteByUserId(user.getId());
    }

    public ResetPasswordToken instantiateResetPasswordToken(String token, User user) {
        return new ResetPasswordToken(token, user);
    }

    public ResetPasswordToken generateNewResetPasswordToken(User user) {
        ResetPasswordToken newToken = this.instantiateResetPasswordToken(UUID.randomUUID().toString(), user);
        this.updateUserResetPasswordToken(newToken.getResetPasswordToken(), user.getId());
        return newToken;
    }

    public String validatePasswordResetToken(String token) {
        ResetPasswordToken resetPasswordToken = this.getResetPasswordToken(token);

        return resetPasswordToken == null ? "Invalid token" :
                resetPasswordToken.getExpiresAt().isBefore(LocalDateTime.now()) ? "Expired"
                        : null;
    }

    public boolean comparePasswords(String userPassword,String oldPassword){
        return passwordEncoder.matches(oldPassword,userPassword);
    }

}
