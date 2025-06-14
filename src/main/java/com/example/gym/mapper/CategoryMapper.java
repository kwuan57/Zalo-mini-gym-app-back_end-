package com.example.gym.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.gym.dto.request.CategoryRequest;
import com.example.gym.dto.request.Update.CategoryUpdateRequest;
import com.example.gym.dto.response.CategoryResponse;
import com.example.gym.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category maptoCategory(CategoryRequest categoryRequest);

    CategoryResponse maptoCategoryResponse(Category category);

    void updateCategory(CategoryUpdateRequest categoryRequest, @MappingTarget Category category);
}
