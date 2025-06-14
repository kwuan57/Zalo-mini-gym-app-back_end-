package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.HealthDataRequest;
import com.example.gym.dto.request.Update.HealthDataUpdateRequest;
import com.example.gym.dto.response.HealthDataResponse;
import com.example.gym.entity.HealthData;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE) // Mapper
                                                                                                                             // for
// HealthData
// entity
// and
// DTOs
public interface HealthDataMapper {
    HealthData maptoHealthData(HealthDataRequest healthDataRequest); // Map HealthDataRequest to HealthData entity

    HealthDataResponse toHealthDataResponse(HealthData healthData); // Map HealthData entity to HealthDataResponse DTO

    void updateHealthData(HealthDataUpdateRequest healthDataRequest, @MappingTarget HealthData healthData); // Update
                                                                                                            // HealthData
                                                                                                            // entity
                                                                                                            // with
    // HealthDataRequest data
}
