package com.example.server.mapper;

import com.example.server.dtos.request.CategoryRequest;
import com.example.server.dtos.response.CategoryResponse;
import com.example.server.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryToCategory(CategoryRequest request);
    CategoryResponse categoryToCategoryResponse(Category category);

    void categoryToUpdateCategory(@MappingTarget Category category, CategoryRequest request);
}
