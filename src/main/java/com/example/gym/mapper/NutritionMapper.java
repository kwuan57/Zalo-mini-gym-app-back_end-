package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.NutritionRequest;
import com.example.gym.dto.request.Update.NutritionUpdateRequest;
import com.example.gym.dto.response.NutritionResponse;
import com.example.gym.entity.Nutrition;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface NutritionMapper {
    Nutrition maptoNutrition(NutritionRequest nutritionRequest); // Map NutritionRequest to Nutrition entity

    NutritionResponse toNutritionResponse(Nutrition nutrition); // Map Nutrition entity to NutritionResponse DTO

    void updateNutrition(NutritionUpdateRequest nutritionRequest, @MappingTarget Nutrition nutrition); // Update
                                                                                                       // Nutrition
                                                                                                       // entity with
                                                                                                       // NutritionRequest
                                                                                                       // data
}
