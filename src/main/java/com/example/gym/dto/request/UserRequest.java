package com.example.gym.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @JsonProperty("name")
    String name;

    @Size(min = 10, max = 10, message = "PHONE_NUMBER_INVALID")
    @JsonProperty("phone_number")
    String phoneNumber;

    @Size(min = 8, max = 32, message = "PASSWORD_INVALID")
    @JsonProperty("password")
    String password;

    @JsonProperty("email")
    String email;

    @JsonProperty("role_id")
    Long roleId;

    @JsonProperty("age")
    Integer age;

    @JsonProperty("height")
    Float height;

    @JsonProperty("weight")
    Float weight;

    @JsonProperty("is_active")
    boolean active;

    @JsonProperty("description")
    String description;
}
