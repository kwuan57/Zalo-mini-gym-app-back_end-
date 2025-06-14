package com.example.gym.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.gym.dto.request.UserRequest;
import com.example.gym.dto.request.Update.UserUpdateRequest;
import com.example.gym.dto.response.UserResponse;
import com.example.gym.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User maptoUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserUpdateRequest userRequest, @MappingTarget User user);
}
