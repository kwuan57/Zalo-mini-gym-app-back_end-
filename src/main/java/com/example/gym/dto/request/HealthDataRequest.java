package com.example.gym.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class HealthDataRequest {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("walk")
    Integer walk;

    @JsonProperty("calories")
    Float calories;

    @JsonProperty("heart_rate")
    Integer heartRate;

    @JsonProperty("sleep")
    String sleep;

    @JsonProperty("date")
    LocalDate date;
}
