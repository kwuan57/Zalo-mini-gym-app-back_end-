package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.ExerciseRequest;
import com.example.gym.dto.request.Update.ExerciseUpdateRequest;
import com.example.gym.dto.response.ExerciseResponse;
import com.example.gym.entity.Exercise;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    Exercise maptoExercise(ExerciseRequest exerciseRequest);

    ExerciseResponse toExerciseResponse(Exercise exercise);

    void updateExercise(ExerciseUpdateRequest exerciseRequest, @MappingTarget Exercise exercise);
}
