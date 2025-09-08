package com.daviddai.blog.service.impl;

import com.daviddai.blog.dto.request.CategoryRequest;
import com.daviddai.blog.dto.response.CategoryResponse;
import com.daviddai.blog.entity.Category;
import com.daviddai.blog.exception.CategoryNotFoundException;
import com.daviddai.blog.mapper.CategoryMapper;
import com.daviddai.blog.repository.CategoryRepository;
import com.daviddai.blog.repository.PostRepository;
import com.daviddai.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryMapper.mapToDto(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
        return categoryMapper.mapToDto(categoryRepository.save(category));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CategoryResponse updateCategory(String id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        return categoryMapper.mapToDto(categoryRepository.save(category));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteCategory(String id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        if (postRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Cannot delete category that is referenced by posts");
        }

        categoryRepository.deleteById(id);
    }
}
