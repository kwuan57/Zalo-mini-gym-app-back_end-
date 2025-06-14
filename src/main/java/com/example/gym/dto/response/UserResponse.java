package com.example.gym.dto.response;

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
public class UserResponse {
    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("email")
    String email;

    @JsonProperty("password")
    String password;

    @JsonProperty("age")
    Integer age;

    @JsonProperty("height")
    Float height;

    @JsonProperty("weight")
    Float weight;

    @JsonProperty("role_id")
    Long roleId;

    @JsonProperty("is_active")
    boolean active;

    @JsonProperty("description")
    String description;
}
