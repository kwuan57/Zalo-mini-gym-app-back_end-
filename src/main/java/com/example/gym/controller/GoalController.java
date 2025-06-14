package com.example.gym.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.dto.request.GoalRequest;
import com.example.gym.dto.request.Update.GoalUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.GoalResponse;
import com.example.gym.entity.Goal;
import com.example.gym.service.GoalService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/goals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoalController {
    GoalService goalService;

    @PostMapping
    ApiResponse<GoalResponse> createGoal(@RequestBody GoalRequest goalRequest) {
        return ApiResponse.<GoalResponse>builder()
                .result(goalService.createGole(goalRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<GoalResponse>> getAllGoals() {
        return ApiResponse.<List<GoalResponse>>builder()
                .result(goalService.getAllGoals())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<Goal>> getAllGoalsByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<Goal>>builder()
                .result(goalService.getAllGoalsByUserId(userId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<GoalResponse> updateGoal(@PathVariable Long id, @RequestBody GoalUpdateRequest goalRequest) {
        return ApiResponse.<GoalResponse>builder()
                .result(goalService.updateGoal(id, goalRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ApiResponse.<String>builder()
                .result("Goal deleted successfully")
                .build();
    }
}
