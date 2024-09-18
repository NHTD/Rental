package com.example.server.services;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.request.CategoryRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.dtos.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getCategories();
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
