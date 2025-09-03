package com.daviddai.blog.service;

import org.springframework.security.core.Authentication;

public interface PostEngagementService {
    void incrementViews(String postId);
    boolean toggleLike(String postId, Authentication authentication);
    long getLikesCount(String postId);
    boolean isLikedByUser(String postId, Authentication authentication);
}