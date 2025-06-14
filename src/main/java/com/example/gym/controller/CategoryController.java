package com.example.gym.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.dto.request.CategoryRequest;
import com.example.gym.dto.request.Update.CategoryUpdateRequest;
import com.example.gym.dto.response.ApiResponse;
import com.example.gym.dto.response.CategoryListResponse;
import com.example.gym.dto.response.CategoryResponse;
import com.example.gym.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
        CategoryService categoryService;

        @PostMapping
        ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
                return ApiResponse.<CategoryResponse>builder()
                                .result(categoryService.createCategory(request))
                                .build();
        }

        @GetMapping("/{id}")
        ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
                return ApiResponse.<CategoryResponse>builder()
                                .result(categoryService.getCategoryById(id))
                                .build();
        }

        @GetMapping
        ApiResponse<List<CategoryResponse>> getAllCategories(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                return ApiResponse.<List<CategoryResponse>>builder()
                                .result(categoryService.getAllCategories())
                                .build();
        }

        @GetMapping("/search")
        ApiResponse<CategoryListResponse> getAllCategories(
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                PageRequest pageRequest = PageRequest.of(
                                page,
                                limit,
                                Sort.by("id").ascending());

                Page<CategoryResponse> categories = categoryService.getAllCategories(keyword, pageRequest);
                List<CategoryResponse> categoryList = categories.getContent();
                int totalPages = categories.getNumber() + 1;
                return ApiResponse.<CategoryListResponse>builder()
                                .result(CategoryListResponse.builder()
                                                .categories(categoryList)
                                                .page(totalPages)
                                                .build())
                                .build();
        }

        @PutMapping("/{id}")
        ApiResponse<CategoryResponse> updateCategory(
                        @PathVariable Long id,
                        @RequestBody @Valid CategoryUpdateRequest request) {
                return ApiResponse.<CategoryResponse>builder()
                                .result(categoryService.updateCategory(id, request))
                                .build();
        }

        @DeleteMapping("/{id}")
        ApiResponse<String> deleteCategory(@PathVariable Long id) {
                categoryService.deleteCategory(id);
                return ApiResponse.<String>builder()
                                .result("Category deleted successfully")
                                .build();
        }

        @GetMapping("/count")
        ApiResponse<Long> getTotalCategoryCount(
                        @RequestParam(required = false) String keyword) {

                return ApiResponse.<Long>builder()
                                .result(categoryService.countCategories(keyword))
                                .build();
        }
}
