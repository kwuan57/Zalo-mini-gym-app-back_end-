package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.MealRequest;
import com.example.gym.dto.response.MealResponse;
import com.example.gym.entity.Meal;

@Mapper(componentModel = "spring")
public interface MealMapper {
    Meal maptoMeal(MealRequest mealRequest);

    MealResponse toMealResponse(Meal meal);

    void updateMeal(MealRequest mealRequest, @MappingTarget Meal meal);
}
