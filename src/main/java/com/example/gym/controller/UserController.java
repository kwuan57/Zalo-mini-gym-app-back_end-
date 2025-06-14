package com.example.gym.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.gym.dto.request.UserImageRequest;
import com.example.gym.dto.request.UserRequest;
import com.example.gym.dto.request.Update.UserUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.UserResponse;
import com.example.gym.entity.UserImage;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.service.FileStorageService;
import com.example.gym.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    FileStorageService fileStorageService;

    String IMAGE_DIRECTORY = "uploads";

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.creatUser(userRequest))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, userRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(IMAGE_DIRECTORY).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new AppException(ErrorCode.INVALID_FILE);
            }

            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_FILE);
        }
    }

    @PostMapping("/uploads/{id}")
    public ApiResponse<?> uploadUserImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.getSize() == 0) {
                throw new AppException(ErrorCode.INVALID_IMAGE);
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
                throw new AppException(ErrorCode.ERROR_SIZE_IMAGE);
            }

            if (!fileStorageService.isImageFile(file)) {
                throw new AppException(ErrorCode.INVALID_IMAGE);
            }

            String fileName = fileStorageService.storeFile(file);

            // Tạo UserImageRequest
            UserImageRequest userImageRequest = UserImageRequest.builder()
                    .url(fileName)
                    .build();

            // Gọi service để lưu ảnh đại diện
            UserImage userImage = userService.uploadAvatarUser(id, userImageRequest);

            // Trả về response
            return ApiResponse.<UserImage>builder()
                    .result(userImage)
                    .build();

        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }

    }
}
