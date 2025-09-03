package com.daviddai.blog.service;

import com.daviddai.blog.dto.request.CategoryRequest;
import com.daviddai.blog.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getCategoryById(String id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(String id, CategoryRequest categoryRequest);

    void deleteCategory(String id);

}
