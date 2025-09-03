package com.daviddai.blog.controller;

import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.PostEngagementResponse;
import com.daviddai.blog.service.PostEngagementService;
import com.daviddai.blog.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/engagement")
@RequiredArgsConstructor
public class PostEngagementController {

    private final PostEngagementService postEngagementService;

    @PostMapping("/view")
    public ResponseEntity<ApiResponse<Void>> incrementViews(@PathVariable String postId) {
        postEngagementService.incrementViews(postId);
        return ApiResponseBuilder.success(null, "Post view incremented successfully");
    }

    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PostEngagementResponse>> toggleLike(
            @PathVariable String postId,
            Authentication authentication) {
        boolean liked = postEngagementService.toggleLike(postId, authentication);
        long likesCount = postEngagementService.getLikesCount(postId);
        
        PostEngagementResponse response = PostEngagementResponse.builder()
                .postId(postId)
                .likesCount(likesCount)
                .liked(liked)
                .build();
                
        return ApiResponseBuilder.success(response, "Post like status updated successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostEngagementResponse>> getEngagement(
            @PathVariable String postId,
            Authentication authentication) {
        long likesCount = postEngagementService.getLikesCount(postId);
        boolean liked = postEngagementService.isLikedByUser(postId, authentication);
        
        PostEngagementResponse response = PostEngagementResponse.builder()
                .postId(postId)
                .likesCount(likesCount)
                .liked(liked)
                .build();
                
        return ApiResponseBuilder.success(response, "Post engagement retrieved successfully");
    }
}