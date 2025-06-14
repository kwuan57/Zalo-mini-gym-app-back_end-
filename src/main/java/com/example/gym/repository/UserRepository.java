package com.example.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
