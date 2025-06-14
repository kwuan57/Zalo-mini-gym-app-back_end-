package com.example.gym.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.gym.dto.request.ExerciseRequest;
import com.example.gym.dto.request.Update.ExerciseUpdateRequest;
import com.example.gym.dto.response.ExerciseResponse;
import com.example.gym.entity.Category;
import com.example.gym.entity.Exercise;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.ExerciseMapper;
import com.example.gym.repository.CategoryReposity;
import com.example.gym.repository.ExerciseRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseService {
    ExerciseRepository exerciseRepository;
    CategoryReposity categoryRepository;
    ExerciseMapper exerciseMapper;

    public ExerciseResponse createExercise(ExerciseRequest request) {
        Category existingCategory = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID));

        if (exerciseRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.EXERCISE_ALREADY_EXISTS);
        }

        Exercise exercise = exerciseMapper.maptoExercise(request);
        exercise.setCategory(existingCategory);
        return exerciseMapper.toExerciseResponse(exerciseRepository.save(exercise));
    }

    public ExerciseResponse getExerciseId(Long id) {
        return exerciseMapper.toExerciseResponse(exerciseRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID)));
    }

    public Page<ExerciseResponse> getAllExercises(String keyword, Long categoryId, PageRequest pageRequest) {
        return exerciseRepository.findAll(pageRequest, categoryId, keyword).map(exerciseMapper::toExerciseResponse);
    }

    public ExerciseResponse updateExercise(ExerciseUpdateRequest request, Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID));
        exerciseMapper.updateExercise(request, exercise);
        return exerciseMapper.toExerciseResponse(exerciseRepository.save(exercise));
    }

    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID));
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findByExerciseIds(List<Long> exerciseIds) {
        return exerciseRepository.findByIds(exerciseIds);
    }

    public Long getTotalExerciseCount(Long categoryId, String keyword) {
        return exerciseRepository.countExercises(categoryId, keyword);
    }
}
