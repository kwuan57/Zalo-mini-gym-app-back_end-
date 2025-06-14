package com.example.gym.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.dto.request.MealRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.CategoryListResponse;
import com.example.gym.dto.response.CategoryResponse;
import com.example.gym.dto.response.MealListResponse;
import com.example.gym.dto.response.MealResponse;
import com.example.gym.service.MealService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/meals")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealController {
        MealService mealService;

        @PostMapping
        ApiResponse<MealResponse> createMeal(@RequestBody MealRequest mealRequest) {
                return ApiResponse.<MealResponse>builder()
                                .result(mealService.createMeal(mealRequest))
                                .build();
        }

        @GetMapping("/{id}")
        ApiResponse<MealResponse> getMealById(@PathVariable Long id) {
                return ApiResponse.<MealResponse>builder()
                                .result(mealService.getMealById(id))
                                .build();
        }

        @GetMapping
        ApiResponse<MealListResponse> getAllExercises(
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                PageRequest pageRequest = PageRequest.of(
                                page,
                                limit,
                                Sort.by("id").ascending());

                Page<MealResponse> mealResponses = mealService.getAllMeals(keyword, pageRequest);
                List<MealResponse> mealList = mealResponses.getContent();
                int totalPages = mealResponses.getNumber() + 1;
                return ApiResponse.<MealListResponse>builder()
                                .result(MealListResponse.builder()
                                                .meals(mealList)
                                                .page(totalPages)
                                                .build())
                                .build();
        }

        @PutMapping("/{id}")
        ApiResponse<MealResponse> updateMeal(@PathVariable Long id, @RequestBody MealRequest mealRequest) {
                return ApiResponse.<MealResponse>builder()
                                .result(mealService.updateMeal(id, mealRequest))
                                .build();
        }

        @DeleteMapping("/{id}")
        ApiResponse<String> deleteMeal(@PathVariable Long id) {
                mealService.deleteMeal(id);
                return ApiResponse.<String>builder()
                                .result("Deleted successfully")
                                .build();
        }

        @GetMapping("/count")
        ApiResponse<Long> getTotalMealCount(@RequestParam(required = false) String keyword) {
                return ApiResponse.<Long>builder()
                                .result(mealService.countMeals(keyword))
                                .build();
        }
}
