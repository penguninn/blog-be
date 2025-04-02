package com.daviddai.blog.services;

import java.util.List;

import com.daviddai.blog.model.dtos.requset.PostRequest;
import com.daviddai.blog.model.dtos.response.PostResponse;

public interface PostService {

    public PostResponse getPostBySlug(String slug);
    public PostResponse getPostById(String id);
    public List<PostResponse> getAllPost();
    public List<PostResponse> getAllPostByTitle(String title);
    public PostResponse createPost(PostRequest postRequest);
    public PostResponse updatePost(String id, PostRequest postRequest);
    public void deletePost(String id);
}
