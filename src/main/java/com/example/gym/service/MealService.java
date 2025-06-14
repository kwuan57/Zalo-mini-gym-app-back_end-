package com.example.gym.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.gym.dto.request.MealRequest;
import com.example.gym.dto.response.ExerciseResponse;
import com.example.gym.dto.response.MealResponse;
import com.example.gym.entity.Meal;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.MealMapper;
import com.example.gym.repository.MealRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealService {
    MealRepository mealRepository;
    MealMapper mealMapper;

    public MealResponse createMeal(MealRequest mealRequest) {
        if (mealRepository.existsByName(mealRequest.getName())) {
            throw new AppException(ErrorCode.MEAL_EXIST);
        }

        Meal meal = mealMapper.maptoMeal(mealRequest);
        return mealMapper.toMealResponse(mealRepository.save(meal));
    }

    public Page<MealResponse> getAllMeals(String keyword, PageRequest pageRequest) {
        return mealRepository.findAll(pageRequest, keyword).map(mealMapper::toMealResponse);
    }

    public MealResponse getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return mealMapper.toMealResponse(meal);
    }

    public MealResponse updateMeal(Long id, MealRequest mealRequest) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        mealMapper.updateMeal(mealRequest, meal);

        return mealMapper.toMealResponse(mealRepository.save(meal));
    }

    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        mealRepository.delete(meal);
    }

    public Long countMeals(String keyword) {
        return mealRepository.countMeals(keyword);
    }
}
