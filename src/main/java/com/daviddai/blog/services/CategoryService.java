package com.daviddai.blog.services;

import java.util.List;

import com.daviddai.blog.model.dtos.response.CategoryResponse;
import com.daviddai.blog.model.entities.Category;

public interface CategoryService {

    public CategoryResponse getCategoryById(String id);

    public List<CategoryResponse> getAllCategories();

    public CategoryResponse createCategory(Category category);

    public CategoryResponse updateCategory(String id, Category category);

    public void deleteCategory(String id);

}
