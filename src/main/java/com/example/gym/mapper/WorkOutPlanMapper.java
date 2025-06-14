package com.example.gym.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.Update.WorkOutPlanUpdateRequest;
import com.example.gym.dto.request.WorkOutPlanRequest;
import com.example.gym.dto.response.WorkOutPlanResponse;
import com.example.gym.entity.WorkOutPlan;

@Mapper(componentModel = "spring")
public interface WorkOutPlanMapper {
    WorkOutPlan maptWorkOutPlan(WorkOutPlanRequest workOutPlanRequest);

    WorkOutPlanResponse toWorkOutPlanResponse(WorkOutPlan workOutPlan);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateWorkOutPlan(WorkOutPlanUpdateRequest workOutPlanRequest, @MappingTarget WorkOutPlan workOutPlan);
}
