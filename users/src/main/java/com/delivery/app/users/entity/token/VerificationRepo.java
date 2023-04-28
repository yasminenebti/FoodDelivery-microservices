package com.delivery.app.users.entity.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepo extends JpaRepository<VerificationToken,Integer> {
    Optional<VerificationToken> findByToken(String token);
}
