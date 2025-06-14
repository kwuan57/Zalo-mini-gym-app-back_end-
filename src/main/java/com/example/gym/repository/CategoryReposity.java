package com.example.gym.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gym.entity.Category;

public interface CategoryReposity extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE (:keyword IS NULL OR :keyword = '' OR c.name LIKE %:keyword%)")
    Page<Category> findAll(
            Pageable pageable,
            @Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM Category c WHERE (:keyword IS NULL OR :keyword = '' OR c.name LIKE %:keyword%)")
    Long countCategories(@Param("keyword") String keyword);
}
