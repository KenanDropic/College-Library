package com.library.service;

import com.library.entity.User;
import com.library.entity.VerificationToken;
import com.library.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class VerificationTokenService {
    private final VerificationTokenRepository vTokenRepo;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepo) {
        this.vTokenRepo = verificationTokenRepo;
    }

    public VerificationToken getVerificationTokenByUser(User user){
        if(vTokenRepo.findByUser(user) != null){
            return vTokenRepo.findByUser(user);
        }
        return null;
    }

    public VerificationToken getVerificationToken(final String token) {
        return vTokenRepo.findByConfirmEmailToken(token);
    }

    public void createVerificationToken(User user, final String token) {
        VerificationToken vToken = new VerificationToken(token, user);
        vTokenRepo.save(vToken);
    }

    public void updateUserVerificationToken(String token,Long userId){
        this.vTokenRepo.updateVerificationToken(token,userId);
    }

    public void deleteVerificationToken(User user){
        vTokenRepo.deleteByUserId(user.getId());
    }

    public VerificationToken instantiateVerificationToken(String token,User user){
        return new VerificationToken(token,user);
    }

    public VerificationToken generateNewVerificationToken(User user) {
        VerificationToken newToken = this.instantiateVerificationToken(UUID.randomUUID().toString(),user);
        this.updateUserVerificationToken(newToken.getConfirmEmailToken(),user.getId());
        return newToken;
    }
}
