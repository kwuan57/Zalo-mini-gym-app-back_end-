package com.example.gym.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.dto.request.HealthDataRequest;
import com.example.gym.dto.request.Update.HealthDataUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.HealthDataResponse;
import com.example.gym.service.HealthDataService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/health-data")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HealthDataController {
    HealthDataService healthDataService;

    @PostMapping
    ResponseEntity<?> createHealthData(@RequestBody HealthDataRequest healthDataRequest) {
        try {
            ApiResponse<HealthDataResponse> response = ApiResponse.<HealthDataResponse>builder()
                    .result(healthDataService.createHealthData(healthDataRequest))
                    .build();
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Health data for this user and date already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while saving health data: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    ApiResponse<HealthDataResponse> getHealthDataByUserId(@PathVariable Long userId) {
        return ApiResponse.<HealthDataResponse>builder()
                .result(healthDataService.getHealthDataByUserId(userId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<HealthDataResponse> updateHealthData(@PathVariable Long id,
            @RequestBody HealthDataUpdateRequest healthDataRequest) {
        return ApiResponse.<HealthDataResponse>builder()
                .result(healthDataService.updateHealthData(id, healthDataRequest))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<HealthDataResponse> getHealthDataById(@PathVariable Long id) {
        return ApiResponse.<HealthDataResponse>builder()
                .result(healthDataService.getHealthDataById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteHealthData(@PathVariable Long id) {
        healthDataService.deleteHealthData(id);
        return ApiResponse.<String>builder()
                .result("Health data deleted successfully")
                .build();
    }

    @GetMapping("/period")
    ApiResponse<HealthDataResponse> getHealthDataByPeriod(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(defaultValue = "today") String period,
            @RequestParam(required = false) String date) {
        return ApiResponse.<HealthDataResponse>builder()
                .result(healthDataService.getHealthDataByPeriod(userId, period, date))
                .build();
    }
}