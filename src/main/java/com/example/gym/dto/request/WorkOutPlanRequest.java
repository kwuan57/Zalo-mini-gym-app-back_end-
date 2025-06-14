package com.example.gym.dto.request;

import java.time.LocalDate;

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
public class WorkOutPlanRequest {
    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("exercise_id")
    Long exerciseId;

    @JsonProperty("date")
    LocalDate date;

    @JsonProperty("duration")
    Integer duration;

}
