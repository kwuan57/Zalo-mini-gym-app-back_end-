package com.example.gym.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

import com.example.gym.dto.request.ExerciseRequest;
import com.example.gym.dto.request.Update.ExerciseUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.ExerciseListResponse;
import com.example.gym.dto.response.ExerciseResponse;
import com.example.gym.entity.Exercise;
import com.example.gym.service.ExerciseService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {
        ExerciseService exerciseService;

        @PostMapping
        ApiResponse<ExerciseResponse> createExercise(@RequestBody @Valid ExerciseRequest request) {
                return ApiResponse.<ExerciseResponse>builder()
                                .result(exerciseService.createExercise(request))
                                .build();
        }

        @GetMapping
        ApiResponse<ExerciseListResponse> getAllExercises(
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                PageRequest pageRequest = PageRequest.of(
                                page,
                                limit,
                                Sort.by("id").ascending());

                Page<ExerciseResponse> exercises = exerciseService.getAllExercises(keyword, categoryId, pageRequest);
                List<ExerciseResponse> exerciseList = exercises.getContent();
                int totalPages = exercises.getNumber() + 1;
                return ApiResponse.<ExerciseListResponse>builder()
                                .result(ExerciseListResponse.builder()
                                                .exercises(exerciseList)
                                                .page(totalPages)
                                                .build())
                                .build();
        }

        @GetMapping("/{id}")
        ApiResponse<ExerciseResponse> getExerciseById(@PathVariable Long id) {
                return ApiResponse.<ExerciseResponse>builder()
                                .result(exerciseService.getExerciseId(id))
                                .build();
        }

        @PutMapping("/{id}")
        ApiResponse<ExerciseResponse> updateExercise(@PathVariable Long id,
                        @RequestBody @Valid ExerciseUpdateRequest request) {
                return ApiResponse.<ExerciseResponse>builder()
                                .result(exerciseService.updateExercise(request, id))
                                .build();
        }

        @DeleteMapping("/{id}")
        ApiResponse<String> deleteExercise(@PathVariable Long id) {
                exerciseService.deleteExercise(id);
                return ApiResponse.<String>builder()
                                .result("Exercise deleted successfully")
                                .build();
        }

        @GetMapping("/by-ids")
        ApiResponse<?> findByExerciseIds(@RequestParam String ids) {
                try {
                        List<Long> exerciseIds = Arrays.stream(ids.split(","))
                                        .map(String::trim)
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());
                        return ApiResponse.<List<Exercise>>builder()
                                        .result(exerciseService.findByExerciseIds(exerciseIds))
                                        .build();

                } catch (Exception e) {
                        return ApiResponse.<String>builder()
                                        .result("Invalid exercise IDs format")
                                        .build();
                }
        }

        @GetMapping("/count")
        ApiResponse<Long> getTotalExerciseCount(
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long categoryId) {
                return ApiResponse.<Long>builder()
                                .result(exerciseService.getTotalExerciseCount(categoryId, keyword))
                                .build();
        }
}
