package com.daviddai.blog.controller;

import com.daviddai.blog.dto.request.CategoryRequest;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.CategoryResponse;
import com.daviddai.blog.service.CategoryService;
import com.daviddai.blog.utils.ApiResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ApiResponseBuilder.success(categories, "Categories retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable String id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ApiResponseBuilder.success(category, "Category retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse category = categoryService.createCategory(categoryRequest);
        return ApiResponseBuilder.created(category, "Category created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse category = categoryService.updateCategory(id, categoryRequest);
        return ApiResponseBuilder.success(category, "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponseBuilder.noContent("Category deleted successfully");
    }
}
