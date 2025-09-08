package com.daviddai.blog.service;

import com.daviddai.blog.dto.request.PostCreateRequest;
import com.daviddai.blog.dto.request.PostUpdateRequest;
import com.daviddai.blog.dto.response.PageResponse;
import com.daviddai.blog.dto.response.PostResponse;
import com.daviddai.blog.enums.PostSortBy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

public interface PostService {
    PostResponse getPostById(String id, Authentication authentication);
    
    PostResponse getPostBySlug(String slug, Authentication authentication);

    PageResponse<PostResponse> getAllPosts(int pageNo, int pageSize, PostSortBy sortBy,
            Sort.Direction direction, Authentication authentication);

    PostResponse createPost(PostCreateRequest request, Authentication authentication);

    PostResponse updatePost(String id, PostUpdateRequest request, Authentication authentication);

    void deletePost(String id, Authentication authentication);

    PageResponse<PostResponse> getPostsByCategory(String categoryId, int pageNo, int pageSize,
            PostSortBy sortBy, Sort.Direction direction, Authentication authentication);


    PageResponse<PostResponse> searchPosts(String query, int pageNo, int pageSize,
            PostSortBy sortBy, Sort.Direction direction, Authentication authentication);
}
