package com.daviddai.blog.controller;

import com.daviddai.blog.dto.request.PostCreateRequest;
import com.daviddai.blog.dto.request.PostPublishRequest;
import com.daviddai.blog.dto.request.PostSearchRequest;
import com.daviddai.blog.dto.request.PostUpdateRequest;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.PageResponse;
import com.daviddai.blog.dto.response.PostResponse;
import com.daviddai.blog.enums.PostSortBy;
import com.daviddai.blog.enums.PostStatus;
import com.daviddai.blog.service.PostService;
import com.daviddai.blog.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "CREATED_AT") PostSortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            Authentication authentication) {

        PageResponse<PostResponse> pageResponse = postService.getAllPosts(page, size, sortBy, direction, authentication);
        return ApiResponseBuilder.successWithPagination(pageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(
            @PathVariable String id,
            Authentication authentication) {

        PostResponse post = postService.getPostById(id, authentication);
        return ApiResponseBuilder.success(post, "Post retrieved successfully");
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostBySlug(
            @PathVariable String slug,
            Authentication authentication) {
        PostResponse post = postService.getPostBySlug(slug, authentication);
        return ApiResponseBuilder.success(post, "Post retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchPosts(
            @RequestParam @Size(min = 1, max = 100) String query,
            @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "CREATED_AT") PostSortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            Authentication authentication) {

        PageResponse<PostResponse> pageResponse = postService.searchPosts(
                query.trim(), page, size, sortBy, direction, authentication);
        return ApiResponseBuilder.successWithPagination(pageResponse);
    }

    @GetMapping("/categories/{categoryId}/posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "CREATED_AT") PostSortBy sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            Authentication authentication) {

        PageResponse<PostResponse> pageResponse = postService.getPostsByCategory(
                categoryId, page, size, sortBy, direction, authentication);
        return ApiResponseBuilder.successWithPagination(pageResponse);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request,
            Authentication authentication) {
        PostResponse post = postService.createPost(request, authentication);
        return ApiResponseBuilder.created(post, "Post created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable String id,
            @Valid @RequestBody PostUpdateRequest request,
            Authentication authentication) {
        PostResponse post = postService.updatePost(id, request, authentication);
        return ApiResponseBuilder.success(post, "Post updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable String id,
            Authentication authentication) {
        postService.deletePost(id, authentication);
        return ApiResponseBuilder.noContent("Post deleted successfully");
    }
}
