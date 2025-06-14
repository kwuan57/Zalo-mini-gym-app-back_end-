package com.example.gym.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.dto.request.NutritionRequest;
import com.example.gym.dto.request.Update.NutritionUpdateRequest;
import com.example.gym.dto.response.NutritionResponse;
import com.example.gym.entity.Meal;
import com.example.gym.entity.Nutrition;
import com.example.gym.entity.User;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.NutritionMapper;
import com.example.gym.repository.MealRepository;
import com.example.gym.repository.NutritionRepository;
import com.example.gym.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NutritionService {
    NutritionRepository nutritionRepository;
    UserRepository userRepository;
    NutritionMapper nutritionMapper;
    MealRepository mealRepository;

    public NutritionResponse createNutrition(NutritionRequest nutritionRequest) {
        User user = userRepository.findById(nutritionRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Meal meal = mealRepository.findById(nutritionRequest.getMealId())
                .orElseThrow(() -> new AppException(ErrorCode.MEAL_NOT_FOUND));

        if (nutritionRequest.getMealType() == null || nutritionRequest.getMealType().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_MEAL_TYPE);
        }

        if (nutritionRepository.existsByMealTypeAndDateAndUserId(
                nutritionRequest.getMealType(),
                nutritionRequest.getDate(),
                user.getId())) {
            throw new AppException(ErrorCode.INVALID_MEAL_TYPE);
        }

        Nutrition nutrition = nutritionMapper.maptoNutrition(nutritionRequest);
        nutrition.setUser(user);
        nutrition.setMeal(meal);
        return nutritionMapper.toNutritionResponse(nutritionRepository.save(nutrition));
    }

    public List<Nutrition> getAllNutrition(Long userId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return nutritionRepository.findByUserIdAndDate(user.getId(), date);
    }

    public NutritionResponse updateNutrition(Long id, NutritionUpdateRequest nutritionRequest) {
        // Tìm bản ghi Nutrition theo ID
        Nutrition nutrition = nutritionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NUTRITION_NOT_FOUND));

        // Kiểm tra nếu mealType đã tồn tại cho user vào ngày cụ thể, nhưng bỏ qua bản
        // ghi hiện tại
        if (nutritionRequest.getMealType() != null && !nutritionRequest.getMealType().equals(nutrition.getMealType())) {
            if (nutritionRepository.existsByMealTypeAndDateAndUserIdAndIdNot(
                    nutritionRequest.getMealType(),
                    nutritionRequest.getDate() != null ? nutritionRequest.getDate() : nutrition.getDate(),
                    nutrition.getUser().getId(),
                    id)) {
                throw new AppException(ErrorCode.INVALID_MEAL_TYPE);
            }
        }

        // Sử dụng mapper để cập nhật các trường từ NutritionUpdateRequest
        nutritionMapper.updateNutrition(nutritionRequest, nutrition);

        // Lưu bản ghi đã cập nhật vào cơ sở dữ liệu
        return nutritionMapper.toNutritionResponse(nutritionRepository.save(nutrition));
    }

    public void deleteNutrition(Long id) {
        Nutrition nutrition = nutritionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        nutritionRepository.delete(nutrition);
    }
}
