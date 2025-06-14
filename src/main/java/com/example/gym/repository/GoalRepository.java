package com.example.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    boolean existsByUserIdAndName(Long userId, String name);

    List<Goal> findAllByUserId(Long userId);
}
