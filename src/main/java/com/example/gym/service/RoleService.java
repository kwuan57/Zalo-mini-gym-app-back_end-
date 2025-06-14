package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.dto.request.RoleRequest;
import com.example.gym.dto.response.RoleResponse;
import com.example.gym.entity.Role;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.RoleMapper;
import com.example.gym.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }
        Role role = roleMapper.maptoRole(roleRequest);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
}
