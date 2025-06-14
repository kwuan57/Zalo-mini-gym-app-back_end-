package com.example.gym.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealRequest {
    @JsonProperty("name")
    String name;

    @JsonProperty("calories")
    Float calories;

    @JsonProperty("fat")
    Float fat;

    @JsonProperty("carbs")
    Float carbs;

    @JsonProperty("protein")
    Float protein;

    @JsonProperty("image_url")
    String imageUrl;
}
