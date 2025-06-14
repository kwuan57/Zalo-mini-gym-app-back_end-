package com.example.gym.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Add this import for StringUtils

import com.example.gym.dto.request.HealthDataRequest;
import com.example.gym.dto.request.Update.HealthDataUpdateRequest;
import com.example.gym.dto.response.HealthDataResponse;
import com.example.gym.entity.HealthData;
import com.example.gym.entity.User;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.HealthDataMapper;
import com.example.gym.repository.HealthDataRepository;
import com.example.gym.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HealthDataService {
    HealthDataRepository healthDataRepository;
    HealthDataMapper healthDataMapper;
    UserRepository userRepository;

    public HealthDataResponse createHealthData(HealthDataRequest healthDataRequest) {
        Long userId = healthDataRequest.getUserId();
        LocalDate today = LocalDate.now();
        if (healthDataRepository.findByUserIdAndDate(userId, today).isPresent()) {
            throw new AppException(ErrorCode.HEALTH_DATA_EXISTS);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        HealthData healthData = healthDataMapper.maptoHealthData(healthDataRequest);
        healthData.setUser(user);
        healthData.setDate(today);
        return healthDataMapper.toHealthDataResponse(healthDataRepository.save(healthData));
    }

    public HealthDataResponse getHealthDataByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        HealthData healthData = healthDataRepository.findByUserId(userId);
        if (healthData == null) {
            throw new AppException(ErrorCode.HEALTH_DATA_NOT_FOUND);
        }

        return healthDataMapper.toHealthDataResponse(healthData);
    }

    public HealthDataResponse updateHealthData(Long userId, HealthDataUpdateRequest healthDataRequest) {
        LocalDate today = LocalDate.now();
        Optional<HealthData> existingData = healthDataRepository.findByUserIdAndDate(userId, today);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        HealthData healthData;
        if (existingData.isEmpty()) {
            throw new AppException(ErrorCode.HEALTH_DATA_NOT_FOUND);
        } else {
            healthData = existingData.get();
        }

        healthDataMapper.updateHealthData(healthDataRequest, healthData);
        return healthDataMapper.toHealthDataResponse(healthDataRepository.save(healthData));
    }

    public void deleteHealthData(Long id) {
        HealthData healthData = healthDataRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        healthDataRepository.delete(healthData);
    }

    public HealthDataResponse getHealthDataById(Long id) {
        HealthData healthData = healthDataRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return healthDataMapper.toHealthDataResponse(healthData);
    }

    public HealthDataResponse getHealthDataByPeriod(Long userId, String period, String date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDate targetDate = StringUtils.hasText(date) ? LocalDate.parse(date) : LocalDate.now();

        // Nếu "today", lấy dữ liệu sức khỏe của người dùng cho ngày đó
        if ("today".equalsIgnoreCase(period)) {
            Optional<HealthData> healthData = healthDataRepository.findByUserIdAndDate(userId, targetDate);
            if (healthData.isEmpty()) {
                return null;
            }
            return healthDataMapper.toHealthDataResponse(healthData.get());
        }
        // Nếu "week", lấy dữ liệu tuần của người dùng
        else if ("week".equalsIgnoreCase(period)) {
            LocalDate startOfWeek = targetDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            List<HealthData> weeklyData = healthDataRepository.findByUserIdAndDateBetween(userId, startOfWeek,
                    endOfWeek);
            if (weeklyData.isEmpty()) {
                return null;
            }
            return aggregateWeeklyData(weeklyData);
        }
        // Nếu "month", lấy dữ liệu của tháng của người dùng
        else if ("month".equalsIgnoreCase(period)) {
            LocalDate startOfMonth = targetDate.withDayOfMonth(1);
            LocalDate endOfMonth = targetDate.withDayOfMonth(targetDate.lengthOfMonth());
            List<HealthData> monthlyData = healthDataRepository.findByUserIdAndDateBetween(userId, startOfMonth,
                    endOfMonth);
            if (monthlyData.isEmpty()) {
                return null;
            }
            return aggregateMonthlyData(monthlyData);
        }
        // Các điều kiện khác như day
        else {
            throw new AppException(ErrorCode.INVALID_PERIOD);
        }
    }

    // Tổng hợp dữ liệu theo tháng
    private HealthDataResponse aggregateMonthlyData(List<HealthData> monthlyData) {
        return aggregateWeeklyData(monthlyData); // Đơn giản hóa bằng cách dùng hàm tổng hợp tuần
    }

    // Tổng hợp dữ liệu tuần
    private HealthDataResponse aggregateWeeklyData(List<HealthData> weeklyData) {
        float totalWalk = 0;
        float totalCalories = 0;
        float totalHeartRate = 0;
        float totalSleepMinutes = 0;
        int count = weeklyData.size();

        for (HealthData data : weeklyData) {
            totalWalk += data.getWalk() != null ? data.getWalk() : 0;
            totalCalories += data.getCalories() != null ? data.getCalories() : 0;
            totalHeartRate += data.getHeartRate() != null ? data.getHeartRate() : 0;
            totalSleepMinutes += parseSleepToMinutes(data.getSleep());
        }

        HealthData aggregatedData = new HealthData();
        aggregatedData.setWalk((int) (totalWalk / count));
        aggregatedData.setCalories(totalCalories);
        aggregatedData.setHeartRate((int) (totalHeartRate / count));
        aggregatedData.setSleep(formatMinutesToSleep((int) (totalSleepMinutes / count)));
        aggregatedData.setDate(LocalDate.now());

        return healthDataMapper.toHealthDataResponse(aggregatedData);
    }

    // Chuyển đổi thời gian ngủ sang phút
    private int parseSleepToMinutes(String sleep) {
        if (sleep == null || sleep.isEmpty())
            return 0;
        String[] parts = sleep.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    // Chuyển phút thành thời gian ngủ
    private String formatMinutesToSleep(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}