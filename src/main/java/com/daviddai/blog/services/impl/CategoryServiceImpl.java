package com.daviddai.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.daviddai.blog.exceptions.CategoryNotFoundException;
import com.daviddai.blog.mappers.CategoryMapper;
import com.daviddai.blog.model.dtos.response.CategoryResponse;
import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.repositories.CategoryRepository;
import com.daviddai.blog.services.CategoryService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("CategoryService:getAllCategories execution started");
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream().map(CategoryMapper::mapToDto).toList();
        log.info("CategoryService:getAllCategories execution ended");
        return categories;
    }

    @Override
    public CategoryResponse createCategory(Category category) {
        log.info("CategoryService:createCategory execution started");
        CategoryResponse result = CategoryMapper.mapToDto(categoryRepository.save(category));
        log.info("CategoryService:createCategory execution ended");
        return result;
    }

    @Override
    public CategoryResponse updateCategory(String id, Category category) {
        log.info("CategoryService:updateCategory execution started");
        Category newCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("category not found"));
        newCategory.setName(category.getName());
        CategoryResponse result = CategoryMapper.mapToDto(categoryRepository.save(newCategory));
        log.info("CategoryService:updateCategory execution ended");
        return result;
    }

    @Override
    public void deleteCategory(String id) {
        log.info("CategoryService:deleteCategory execution started");
        categoryRepository.deleteById(id);
        log.info("CategoryService:deleteCategory execution ended");
    }

    @Override
    public CategoryResponse getCategoryById(String id) {
        log.info("CategoryService:getCategory execution started");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("category not found"));
        log.info("CategoryService:getCategory execution ended");
        return CategoryMapper.mapToDto(category);
    }

}
