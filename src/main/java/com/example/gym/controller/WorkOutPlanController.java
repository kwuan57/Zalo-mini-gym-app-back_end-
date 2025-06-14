package com.example.gym.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.dto.request.Update.WorkOutPlanUpdateRequest;
import com.example.gym.dto.request.WorkOutPlanRequest;

import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.WorkOutPlanResponse;
import com.example.gym.entity.Nutrition;
import com.example.gym.entity.WorkOutPlan;
import com.example.gym.service.WorkOutPlanService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/workout-plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkOutPlanController {
    WorkOutPlanService workOutPlanService;

    @PostMapping
    ApiResponse<WorkOutPlanResponse> createWorkOutPlan(@RequestBody WorkOutPlanRequest workOutPlanRequest) {
        return ApiResponse.<WorkOutPlanResponse>builder()
                .result(workOutPlanService.createWorkOutPlan(workOutPlanRequest))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<WorkOutPlanResponse> getWorkOutPlan(@PathVariable Long id) {
        return ApiResponse.<WorkOutPlanResponse>builder()
                .result(workOutPlanService.getWorkOutPlan(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<WorkOutPlan>> getWorkOutPlanByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<WorkOutPlan>>builder()
                .result(workOutPlanService.getWorkOutPlanByUserId(userId))
                .build();
    }

    @GetMapping
    ApiResponse<List<WorkOutPlan>> getAllNutrition(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "date") LocalDate date) {
        return ApiResponse.<List<WorkOutPlan>>builder()
                .result(workOutPlanService.getWorkOutPlanByUserIdAndDate(userId, date))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<WorkOutPlanResponse> updateWorkOutPlan(@PathVariable Long id,
            @RequestBody WorkOutPlanUpdateRequest workOutPlanRequest) {
        return ApiResponse.<WorkOutPlanResponse>builder()
                .result(workOutPlanService.updateWorkOutPlan(id, workOutPlanRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteWorkOutPlan(@PathVariable Long id) {
        workOutPlanService.deleteWorkOutPlan(id);
        return ApiResponse.<String>builder()
                .result("Deleted successfully")
                .build();
    }
}
