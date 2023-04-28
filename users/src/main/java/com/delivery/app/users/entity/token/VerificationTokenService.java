package com.delivery.app.users.entity.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {


    private final VerificationRepo verificationRepo;
    public void saveVerificationToken(VerificationToken token){
        verificationRepo.save(token);

    }

    public String generateVerificationToken(String username){
        VerificationToken verificationToken = VerificationToken
                .builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .confirmedAt(LocalDateTime.now().plusMinutes(10))
                .username(username)
                .build();
        verificationRepo.save(verificationToken);
        return verificationToken.getToken();
    }

    public Optional<VerificationToken> getToken(String token){
        return verificationRepo.findByToken(token);

    }

}
