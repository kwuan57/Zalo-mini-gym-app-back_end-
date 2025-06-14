package com.example.gym.dto.response;

import com.example.gym.entity.Category;
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
public class ExerciseResponse {
    Long id;

    String name;

    String description;

    @JsonProperty("video_url")
    String videoUrl;

    @JsonProperty("category_id")
    Category category;

    @JsonProperty("time")
    String time;

    @JsonProperty("calories")
    Float calories;

    @JsonProperty("level")
    String level;

    @JsonProperty("image_url")
    String imageUrl;
}
