package com.daviddai.blog.controllers;

import com.daviddai.blog.model.dtos.response.CategoryResponse;
import jakarta.validation.Valid;

import com.daviddai.blog.model.dtos.response.ResponseSuccess;
import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.services.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseSuccess<?> getCategory(@Valid @PathVariable String id) {
        log.info("CategoryController::getCategory execution started");
        CategoryResponse category = categoryService.getCategoryById(id);
        log.info("CategoryController::getCategory execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get category successfully", category);
    }
    
    @PostMapping
    public ResponseSuccess<?> createCategory(@Valid @RequestBody Category category) {
        log.info("CategoryController::createCategory execution started");
        CategoryResponse result = categoryService.createCategory(category);
        log.info("CategoryController::createCategory execution ended");
        return new ResponseSuccess<>(HttpStatus.CREATED, "Create new category successfully", result);
    }

    @GetMapping
    public ResponseSuccess<?> getAllCategories() {
        log.info("CategoryController::getAllCategories execution started");
        List<CategoryResponse> categories = categoryService.getAllCategories();
        log.info("CategoryController::getAllCategories execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all category successfully", categories);
    }

    @PutMapping("/{id}")
    public ResponseSuccess<?> updateCategory(@Valid @PathVariable String id, @Valid @RequestBody Category categoryRequest) {
        log.info("CategoryController::updateCategory execution started");
        CategoryResponse category = categoryService.updateCategory(id, categoryRequest);
        log.info("CategoryController::updateCategory execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Update category successfully", category);
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess<?> deleteCategory(@Valid @PathVariable String id) {
        log.info("CategoryController::deleteCategory execution started");
        categoryService.deleteCategory(id);
        log.info("CategoryController::deleteCategory execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Delete category successfully");
    }
}
