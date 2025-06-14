package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.GoalRequest;
import com.example.gym.dto.request.Update.GoalUpdateRequest;
import com.example.gym.dto.response.GoalResponse;
import com.example.gym.entity.Goal;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    Goal maptoGoal(GoalRequest goalRequest);

    GoalResponse toGoalResponse(Goal goal);

    void updateGoal(GoalUpdateRequest goalRequest, @MappingTarget Goal goal);
}
