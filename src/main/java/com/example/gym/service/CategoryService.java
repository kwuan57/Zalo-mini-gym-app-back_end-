package com.example.gym.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.gym.dto.request.CategoryRequest;
import com.example.gym.dto.request.Update.CategoryUpdateRequest;
import com.example.gym.dto.response.CategoryResponse;
import com.example.gym.entity.Category;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.CategoryMapper;
import com.example.gym.repository.CategoryReposity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryReposity categoryReposity;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryReposity.existsByName(categoryRequest.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTS);
        }
        Category category = categoryMapper.maptoCategory(categoryRequest);
        return categoryMapper.maptoCategoryResponse(categoryReposity.save(category));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryReposity.findAll().stream()
                .map(categoryMapper::maptoCategoryResponse)
                .toList();
    }

    public Page<CategoryResponse> getAllCategories(String keyword, PageRequest pageRequest) {
        return categoryReposity.findAll(pageRequest, keyword).map(categoryMapper::maptoCategoryResponse);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryReposity.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_EXISTS));
        return categoryMapper.maptoCategoryResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryRequest) {
        Category category = categoryReposity.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));

        category.setName(categoryRequest.getName());
        categoryMapper.updateCategory(categoryRequest, category);
        return categoryMapper.maptoCategoryResponse(categoryReposity.save(category));
    }

    public void deleteCategory(Long id) {
        Category category = categoryReposity.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        categoryReposity.delete(category);
    }

    public Long countCategories(String keyword) {
        return categoryReposity.countCategories(keyword);
    }

}
