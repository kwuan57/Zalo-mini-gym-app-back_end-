package com.example.gym.dto.request.Update;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ExerciseUpdateRequest {
    String name;
    String description;

    @JsonProperty("video_url")
    String videoUrl;

    @JsonProperty("category_id")
    Long categoryId;

    @JsonProperty("level")
    String level;

    @JsonProperty("image_url")
    String imageUrl;
    @JsonProperty("time")
    String time;
}
