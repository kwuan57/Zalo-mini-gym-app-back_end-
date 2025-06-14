package com.example.gym.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.Nutrition;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {

    List<Nutrition> findByUserIdAndDate(Long userId, LocalDate date); // Find all nutrition records for a specific user
                                                                      // on
                                                                      // a specific date

    boolean existsByMealTypeAndDateAndUserId(String mealType, LocalDate date, Long userId); // Check if a meal type
                                                                                            // already exists for a
                                                                                            // specific user on a
                                                                                            // specific date

    boolean existsByMealTypeAndDateAndUserIdAndIdNot(String mealType, LocalDate date, Long userId, Long id);

}
