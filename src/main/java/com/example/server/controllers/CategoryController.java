package com.example.server.controllers;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.request.CategoryRequest;
import com.example.server.dtos.request.PermissionRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.dtos.response.CategoryResponse;
import com.example.server.dtos.response.PermissionResponse;
import com.example.server.services.CategoryService;
import com.example.server.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.createCategory(request));
    }

    @GetMapping
    ResponseEntity<List<CategoryResponse>> getCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
    }

    @PostMapping("/{id}")
    ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") Long id, CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProvince(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
