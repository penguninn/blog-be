package com.daviddai.blog.controllers;

import jakarta.validation.Valid;

import com.daviddai.blog.model.dtos.request.PostRequest;
import com.daviddai.blog.model.dtos.response.PageResponse;
import com.daviddai.blog.model.dtos.response.PostResponse;
import com.daviddai.blog.model.dtos.response.ResponseSuccess;
import com.daviddai.blog.services.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    public ResponseSuccess<?> getAllPost(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title,asc") String sort) {
        log.info("PostController::getAllPost execution started");
        PageResponse<?> postResponse = postService.getAllPost(page, size, sort);
        log.info("PostController::getAllPost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all post successfully", postResponse);
    }

    @GetMapping("/by-category")
    public ResponseSuccess<?> getAllPostByCategory(
            @RequestParam(required = true) String categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title,asc") String sort) {
        log.info("PostController::getAllPostByCategory execution started");
        PageResponse<?> postResponse = postService.getAllPostByCategory(categoryId, page, size, sort);
        log.info("PostController::getAllPostByCategory execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all post by category successfully", postResponse);
    }

    @GetMapping("/by-tag")
    public ResponseSuccess<?> getAllPostByTag(
            @RequestParam(required = true) String tagId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title,asc") String sort) {
        log.info("PostController::getAllPostByTag execution started");
        PageResponse<?> postResponse = postService.getAllPostByTag(tagId, page, size, sort);
        log.info("PostController::getAllPostByTag execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all post by tag successfully", postResponse);
    }

    @GetMapping("/search")
    public ResponseSuccess<?> getAllPostBySlug(
            @RequestParam(required = true) String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title,asc") String sort) {
        log.info("PostController::getAllPostTitle execution started");
        PageResponse<?> postResponse = postService.getAllPostBySlug(query, page, size, sort);
        log.info("PostController::getAllPostTitle execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all post by title successfully", postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess<?> deletePost(@Valid @PathVariable String id) {
        log.info("PostController::deletePost execution started");
        postService.deletePost(id);
        log.info("PostController::deletePost execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Delete post successfully");
    }
}
