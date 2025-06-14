package com.example.gym.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.Nutrition;
import com.example.gym.entity.WorkOutPlan;

public interface WorkOutPlanRepository extends JpaRepository<WorkOutPlan, Long> {
    WorkOutPlan findByUserIdAndExerciseId(Long userId, Long exerciseId);

    List<WorkOutPlan> findAllByUserId(Long userId);

    boolean existsByUserIdAndExerciseId(Long userId, Long exerciseId);

    List<WorkOutPlan> findByUserIdAndDate(Long userId, LocalDate date);
}
