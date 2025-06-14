package com.example.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.InvalidateToken;

public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {

}
