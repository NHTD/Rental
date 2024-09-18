package com.example.server.services.servicesImp;

import com.example.server.dtos.request.CategoryRequest;
import com.example.server.dtos.response.CategoryResponse;
import com.example.server.exception.RentalHomeDataInvalidException;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.CategoryMapper;
import com.example.server.models.Area;
import com.example.server.models.Category;
import com.example.server.repositories.CategoryRepository;
import com.example.server.services.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImp implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if(categoryRepository.findCategoryByCode(request.getCode()).isPresent()){
            throw new RentalHomeDataInvalidException("This code is existed", request.getCode());
        }
        Category category = categoryMapper.categoryToCategory(request);

        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This id {} is not found", id));

        categoryMapper.categoryToUpdateCategory(category, request);

        return categoryMapper.categoryToCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
