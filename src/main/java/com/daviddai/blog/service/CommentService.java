package com.daviddai.blog.service;

import com.daviddai.blog.dto.request.CommentCreateRequest;
import com.daviddai.blog.dto.request.CommentUpdateRequest;
import com.daviddai.blog.dto.response.CommentResponse;
import com.daviddai.blog.dto.response.PageResponse;
import org.springframework.security.core.Authentication;

public interface CommentService {
    CommentResponse createComment(CommentCreateRequest request, Authentication authentication);
    PageResponse<CommentResponse> getCommentsByPost(String postId, int page, int size);
    CommentResponse updateComment(String commentId, CommentUpdateRequest request, Authentication authentication);
    void deleteComment(String commentId, Authentication authentication);
    CommentResponse getComment(String commentId);
}