package com.daviddai.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.daviddai.blog.exceptions.CategoryNotFoundException;
import com.daviddai.blog.exceptions.PostNotFoundException;
import com.daviddai.blog.mappers.PostMapper;
import com.daviddai.blog.model.dtos.request.PostRequest;
import com.daviddai.blog.model.dtos.response.PageResponse;
import com.daviddai.blog.model.dtos.response.PostResponse;
import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.model.entities.Post;
import com.daviddai.blog.repositories.CategoryRepository;
import com.daviddai.blog.repositories.PostRepository;
import com.daviddai.blog.services.PostService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PostResponse getPostBySlug(String slug) {
        PostResponse postResponse;
        log.info("PostService::getPostBySlug execution started");
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException("post not found"));
        postResponse = PostMapper.mapToDto(post);
        log.info("PostService::getPostBySlug execution ended");
        return postResponse;
    }

    @Override
    public PostResponse getPostById(String id) {
        PostResponse postResponse;
        log.info("PostService::getPostById execution started");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("post not found"));
        postResponse = PostMapper.mapToDto(post);
        log.info("PostService::getPostById execution ended");
        return postResponse;
    }

    @Override
    public PageResponse<?> getAllPost(int pageNo, int pageSize, String sortBy) {
        log.info("PostService::getAllPost execution started");
        int page = pageNo > 0 ? pageNo - 1 : 0;
        String[] sortParams = sortBy.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostMapper.mapToDto(post))
                .collect(Collectors.toList());
        log.info("PostService::getAllPost execution ended");
        return PageResponse.builder()
                .page(posts.getPageable().getPageNumber())
                .size(posts.getPageable().getPageSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        PostResponse postResponse;
        log.info("PostService::createPost execution started");
        Post post = PostMapper.mapToEntity(postRequest);
        postResponse = PostMapper.mapToDto(postRepository.save(post));
        log.info("PostService::createPost execution ended");
        return postResponse;
    }

    @Override
    public PostResponse updatePost(String id, PostRequest postRequest) {
        PostResponse postResponse;
        log.info("PostService::updatePost execution started");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("post not found with id: " + id));
        post.setTitle(postRequest.getTitle());
        post.setSlug(postRequest.getSlug());
        post.setStatus(postRequest.getStatus());
        post.setCategory(postRequest.getCategory());
        post.setTags(postRequest.getTags());
        post.setContents(postRequest.getContents());
        postResponse = PostMapper.mapToDto(postRepository.save(post));
        log.info("PostService::updatePost execution ended");
        return postResponse;
    }

    @Override
    public void deletePost(String id) {
        log.info("PostService::deletePost execution started");
        postRepository.deleteById(id);
        log.info("PostService::deletePost execution ended");
    }

    @Override
    public PageResponse<?> getAllPostByCategory(String categoryId, int pageNo, int pageSize, String sortBy) {
        log.info("PostService::getAllPostByCategory execution started");
        int page = pageNo > 0 ? pageNo - 1 : 0;
        String[] sortParams = sortBy.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("not found category"));
        Page<Post> posts = postRepository.findAllByCategory(category, pageable);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostMapper.mapToDto(post))
                .collect(Collectors.toList());
        log.info("PostService::getAllPostByCategory execution ended");
        return PageResponse.builder()
                .page(posts.getPageable().getPageNumber())
                .size(posts.getPageable().getPageSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    public PageResponse<?> getAllPostByTag(String tagId, int pageNo, int pageSize, String sortBy) {
        log.info("PostService::getAllPostByTag execution started");
        int page = pageNo > 0 ? pageNo - 1 : 0;
        String[] sortParams = sortBy.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);
        Page<Post> posts = postRepository.findAllByTagId(tagId, pageable);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostMapper.mapToDto(post))
                .collect(Collectors.toList());
        log.info("PostService::getAllPostByTag execution ended");
        return PageResponse.builder()
                .page(posts.getPageable().getPageNumber())
                .size(posts.getPageable().getPageSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    public PageResponse<?> getAllPostBySlug(String query, int pageNo, int pageSize, String sortBy) {
        log.info("PostService::getAllPostByTitle execution started");
        int page = pageNo > 0 ? pageNo - 1 : 0;
        String[] sortParams = sortBy.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);
        Page<Post> posts = postRepository.findAllBySlugLike(query, pageable);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostMapper.mapToDto(post))
                .collect(Collectors.toList());
        log.info("PostService::getAllPostByTitle execution ended");
        return PageResponse.builder()
                .page(posts.getPageable().getPageNumber())
                .size(posts.getPageable().getPageSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

}
