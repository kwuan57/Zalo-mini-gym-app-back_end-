package com.example.gym.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gym.entity.Category;
import com.example.gym.entity.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {
    boolean existsByName(String name);

    @Query("SELECT m FROM Meal m WHERE (:keyword IS NULL OR :keyword = '' OR m.name LIKE %:keyword%)")
    Page<Meal> findAll(
            Pageable pageable,
            @Param("keyword") String keyword);

    @Query("SELECT COUNT(m) FROM Meal m WHERE (:keyword IS NULL OR :keyword = '' OR m.name LIKE %:keyword%)")
    Long countMeals(@Param("keyword") String keyword);
}
