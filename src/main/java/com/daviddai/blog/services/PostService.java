package com.daviddai.blog.services;

import com.daviddai.blog.model.dtos.requset.PostRequest;
import com.daviddai.blog.model.dtos.response.PageResponse;
import com.daviddai.blog.model.dtos.response.PostResponse;

public interface PostService {

    public PostResponse getPostBySlug(String slug);
    public PostResponse getPostById(String id);
    public PageResponse<?> getAllPost(int pageNo, int pageSize, String sortBy);
    public PageResponse<?> getAllPostBySlug(String title, int pageNo, int pageSize, String sortBy);
    public PageResponse<?> getAllPostByCategory(String categoryId, int pageNo, int pageSize, String sortBy);
    public PageResponse<?> getAllPostByTag(String tagId, int pageNo, int pageSize, String sortBy);
    public PostResponse createPost(PostRequest postRequest);
    public PostResponse updatePost(String id, PostRequest postRequest);
    public void deletePost(String id);
}
