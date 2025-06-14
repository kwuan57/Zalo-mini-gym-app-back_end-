package com.example.gym.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gym.entity.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);

    @Query("SELECT e FROM Exercise e WHERE (:categoryId IS NULL OR :categoryId = 0 OR e.category.id = :categoryId) AND (:keyword IS NULL OR :keyword = '' OR e.name LIKE %:keyword%)")
    Page<Exercise> findAll(
            Pageable pageable,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword);

    @Query("SELECT e FROM Exercise e WHERE e.id IN :exerciseIds")
    List<Exercise> findByIds(@Param("exerciseIds") List<Long> exerciseIds);

    @Query("SELECT COUNT(e) FROM Exercise e WHERE (:categoryId IS NULL OR :categoryId = 0 OR e.category.id = :categoryId) AND (:keyword IS NULL OR :keyword = '' OR e.name LIKE %:keyword%)")
    Long countExercises(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword);
}
