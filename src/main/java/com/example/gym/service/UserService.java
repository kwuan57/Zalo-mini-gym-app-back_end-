package com.example.gym.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.gym.dto.request.UserImageRequest;
import com.example.gym.dto.request.UserRequest;
import com.example.gym.dto.request.Update.UserUpdateRequest;
import com.example.gym.dto.response.UserResponse;
import com.example.gym.entity.Role;
import com.example.gym.entity.User;
import com.example.gym.entity.UserImage;
import com.example.gym.enums.RolePlay;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.UserMapper;
import com.example.gym.repository.UserImageRepository;
import com.example.gym.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FileStorageService fileStorageService;
    UserImageRepository userImageRepository;

    public UserResponse creatUser(UserRequest userRequest) {
        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTS);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        User user = userMapper.maptoUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role role = new Role();
        role.setId(1L);
        role.setName(RolePlay.USER.name());
        user.setRole(role);
        user.setActive(true);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String phoneNumber = context.getAuthentication().getName();
        log.info("ROLE: {}", context.getAuthentication().getAuthorities());
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        String currentPhoneNumber = user.getPhoneNumber();

        String newPhoneNumber = userRequest.getPhoneNumber();
        if (newPhoneNumber != null && !newPhoneNumber.isEmpty() && !newPhoneNumber.equals(currentPhoneNumber)) {
            if (userRepository.existsByPhoneNumber(newPhoneNumber)) {
                throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
            }
            user.setPhoneNumber(newPhoneNumber);
        }
        String email = userRequest.getEmail();
        String emailCurrent = user.getEmail();
        if (email != null && !email.isEmpty() && !email.equals(emailCurrent)) {
            if (userRepository.existsByEmail(email)) {
                throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
            }
            user.setEmail(email);
        }
        userMapper.updateUser(userRequest, user);
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public UserImage uploadAvatarUser(Long userId, UserImageRequest userImageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));

        // Xử lý ảnh đại diện cũ nếu tồn tại
        UserImage userImage = userImageRepository.findByUserId(userId).orElse(null);
        if (userImage != null) {
            // Xóa file ảnh cũ
            try {
                Path oldFilePath = Paths.get("uploads", userImage.getUrl());
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_DELETION_FAILED);
            }
            // Cập nhật URL mới cho UserImage hiện có
            userImage.setUrl(userImageRequest.getUrl());
        } else {
            // Tạo UserImage mới nếu chưa có
            userImage = UserImage.builder()
                    .user(user)
                    .url(userImageRequest.getUrl())
                    .build();
        }

        // Lưu UserImage (cập nhật hoặc tạo mới)
        userImage = userImageRepository.save(userImage);

        // Cập nhật description của User với URL ảnh
        user.setDescription(userImageRequest.getUrl());
        userRepository.save(user);

        return userImage;
    }

}
