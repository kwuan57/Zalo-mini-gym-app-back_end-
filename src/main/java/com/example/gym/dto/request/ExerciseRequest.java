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
public class ExerciseRequest {
    String name;

    String description;

    @JsonProperty("video_url")
    String videoUrl;

    @JsonProperty("category_id")
    Long categoryId;

    @JsonProperty("time")
    String time;

    @JsonProperty("calories")
    Float calories;

    @JsonProperty("level")
    String level;

    @JsonProperty("image_url")
    String imageUrl;
}
