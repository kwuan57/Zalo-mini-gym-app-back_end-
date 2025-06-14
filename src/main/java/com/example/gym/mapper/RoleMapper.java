package com.example.gym.mapper;

import org.mapstruct.Mapper;

import com.example.gym.dto.request.RoleRequest;
import com.example.gym.dto.response.RoleResponse;
import com.example.gym.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role maptoRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
