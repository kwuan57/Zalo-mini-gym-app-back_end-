package com.example.gym.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gym.entity.HealthData;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

    // Trả về tất cả dữ liệu sức khỏe của người dùng
    HealthData findByUserId(Long userId);

    // Trả về bản ghi mới nhất cho mỗi người dùng trong một ngày nhất định
    Optional<HealthData> findTopByUserIdAndDateOrderByDateDesc(Long userId, LocalDate date);

    // Trả về tất cả dữ liệu sức khỏe của người dùng trong một khoảng thời gian
    List<HealthData> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Trả về dữ liệu sức khỏe của người dùng trong ngày nhất định
    Optional<HealthData> findByUserIdAndDate(Long userId, LocalDate date);
}
