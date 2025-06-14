package com.example.gym.dto.response;

import java.time.LocalDate;

import com.example.gym.entity.Exercise;
import com.example.gym.entity.User;
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
public class WorkOutPlanResponse {
    Long id;

    @JsonProperty("user_id")
    User user;

    @JsonProperty("exercise_id")
    Exercise exercise;

    @JsonProperty("date")
    LocalDate date;

    @JsonProperty("duration")
    Integer duration;

    @JsonProperty("active")
    boolean active;
}
