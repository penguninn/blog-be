package com.daviddai.blog.controllers;

import jakarta.validation.Valid;

import com.daviddai.blog.model.dtos.requset.PostRequest;
import com.daviddai.blog.model.dtos.response.PostResponse;
import com.daviddai.blog.model.dtos.response.ResponseSuccess;
import com.daviddai.blog.services.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/s/{slug}")
    public ResponseSuccess<?> getPost(@Valid @PathVariable String slug) {
        log.info("PostController::getPost execution started");
        PostResponse postResponse = postService.getPostBySlug(slug);
        log.info("PostController::getPost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get post successfully", postResponse);
    }

    @GetMapping("/i/{id}")
    public ResponseSuccess<?> getPostById(@Valid @PathVariable String id) {
        log.info("PostController::getPostById execution started");
        PostResponse postResponse = postService.getPostById(id);
        log.info("PostController::getPostById execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get post successfully", postResponse);
    }
    
    @PostMapping
    public ResponseSuccess<?> createPost(@Valid @RequestBody PostRequest postRequest) {
        log.info("PostController::createPost execution started");
        PostResponse postResponse = postService.createPost(postRequest);
        log.info("PostController::createPost execution ended");
        return new ResponseSuccess<>(HttpStatus.CREATED, "Create new post successfully", postResponse);
    }

    @PutMapping("/{id}")
    public ResponseSuccess<?> updatePost(@Valid @PathVariable String id, @Valid @RequestBody PostRequest postRequest) {
        log.info("PostController::updatePost execution started");
        PostResponse postResponse = postService.updatePost(id, postRequest);
        log.info("PostController::updatePost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Update new post successfully", postResponse);
    }

    @GetMapping
    public ResponseSuccess<?> getAllPost() {
        log.info("PostController::getAllPost execution started");
        List<PostResponse> postResponse = postService.getAllPost();
        log.info("PostController::getAllPost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all post successfully", postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess<?> deletePost(@Valid @PathVariable String id) {
        log.info("PostController::deletePost execution started");
        postService.deletePost(id);
        log.info("PostController::deletePost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Delete post successfully");
    }
}
