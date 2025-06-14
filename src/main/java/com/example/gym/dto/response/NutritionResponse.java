package com.example.gym.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.gym.entity.Meal;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NutritionResponse {

    Long id;

    @JsonProperty("user_id")
    Long userId; // ID of the user who logged the nutrition

    @JsonProperty("meal_id")
    Long mealId;

    @JsonProperty("meal_type")
    String mealType; // breakfast, lunch, dinner, snack

    @JsonProperty("water_intake")
    Float waterIntake; // in liters

    @JsonProperty("created_at")
    LocalDate date; // date of the meal

    @JsonProperty("meal_time")
    LocalTime time; // time of the meal

}
