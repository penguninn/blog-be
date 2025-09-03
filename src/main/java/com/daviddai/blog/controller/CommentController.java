package com.daviddai.blog.controller;

import com.daviddai.blog.dto.request.CommentCreateRequest;
import com.daviddai.blog.dto.request.CommentUpdateRequest;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.CommentResponse;
import com.daviddai.blog.dto.response.PageResponse;
import com.daviddai.blog.service.CommentService;
import com.daviddai.blog.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @Valid @RequestBody CommentCreateRequest request,
            Authentication authentication) {
        CommentResponse response = commentService.createComment(request, authentication);
        return ApiResponseBuilder.created(response, "Comment created successfully");
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<CommentResponse> response = commentService.getCommentsByPost(postId, page, size);
        return ApiResponseBuilder.successWithPagination(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(@PathVariable String commentId) {
        CommentResponse response = commentService.getComment(commentId);
        return ApiResponseBuilder.success(response, "Comment retrieved successfully");
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            Authentication authentication) {
        CommentResponse response = commentService.updateComment(commentId, request, authentication);
        return ApiResponseBuilder.success(response, "Comment updated successfully");
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable String commentId,
            Authentication authentication) {
        commentService.deleteComment(commentId, authentication);
        return ApiResponseBuilder.noContent("Comment deleted successfully");
    }
}