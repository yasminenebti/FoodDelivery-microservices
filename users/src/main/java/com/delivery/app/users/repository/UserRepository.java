package com.delivery.app.users.repository;

import com.delivery.app.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
