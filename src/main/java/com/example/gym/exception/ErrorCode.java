package com.example.gym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_ID(1001, "Invalid ID", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTS(1002, "Category already exists", HttpStatus.BAD_REQUEST),
    EXERCISE_ALREADY_EXISTS(1003, "Exercise already exists", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_EXISTS(1004, "Role already exists", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1005, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    INVALID_GOAL_NAME(1007, "Invalid goal name", HttpStatus.BAD_REQUEST),
    GOAL_ALREADY_EXISTS(1008, "Goal already exists", HttpStatus.BAD_REQUEST),
    INVALID_MEAL_TYPE(1009, "Invalid meal type", HttpStatus.BAD_REQUEST),
    NUTRITION_NOT_FOUND(1010, "Nutrition not found", HttpStatus.NOT_FOUND),
    MEAL_NOT_FOUND(1011, "Meal not found", HttpStatus.NOT_FOUND),
    HEALTH_DATA_NOT_FOUND(1012, "Health data not found", HttpStatus.NOT_FOUND),
    HEALTH_DATA_EXISTS(1013, "Health data already exists", HttpStatus.BAD_REQUEST),
    WORKOUT_PLAN_NOT_FOUND(1014, "Workout plan not found", HttpStatus.NOT_FOUND),
    TRAINER_NOT_FOUND(1015, "Trainer not found", HttpStatus.NOT_FOUND),
    MEAL_EXIST(1016, "Meal already exists", HttpStatus.BAD_REQUEST),
    INVALID_PERIOD(1017, "Invalid period", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(1018, "Invalid date range", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1019, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOT_USER(1020, "User not found", HttpStatus.NOT_FOUND),
    INVALID_KEY(1021, "Invalid key", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTS(1022, "Phone number already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1023, "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_FILE(1024, "Invalid file", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1025, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_IMAGE(1026, "Invalid image", HttpStatus.BAD_REQUEST),
    ERROR_SIZE_IMAGE(1027, "Image size exceeds limit", HttpStatus.BAD_REQUEST),
    FILE_DELETION_FAILED(1028, "File deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
