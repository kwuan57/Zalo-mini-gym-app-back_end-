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

import com.example.gym.dto.request.NutritionRequest;
import com.example.gym.dto.request.Update.NutritionUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.NutritionResponse;
import com.example.gym.entity.Nutrition;
import com.example.gym.service.NutritionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/nutrition")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NutritionController {
    NutritionService nutritionService;

    @PostMapping
    ApiResponse<NutritionResponse> createNutrition(@RequestBody NutritionRequest nutritionRequest) {
        return ApiResponse.<NutritionResponse>builder()
                .result(nutritionService.createNutrition(nutritionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<Nutrition>> getAllNutrition(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "date") LocalDate date) {
        return ApiResponse.<List<Nutrition>>builder()
                .result(nutritionService.getAllNutrition(userId, date))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<NutritionResponse> updateNutrition(@PathVariable Long id,
            @RequestBody NutritionUpdateRequest nutritionRequest) {
        return ApiResponse.<NutritionResponse>builder()
                .result(nutritionService.updateNutrition(id, nutritionRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteNutrition(@PathVariable Long id) {
        nutritionService.deleteNutrition(id);
        return ApiResponse.<String>builder()
                .result("Nutrition with id " + id + " deleted successfully")
                .build();
    }
}
