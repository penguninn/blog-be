package com.daviddai.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.daviddai.blog.exceptions.PostNotFoundException;
import com.daviddai.blog.mappers.PostMapper;
import com.daviddai.blog.mappers.TagMapper;
import com.daviddai.blog.model.dtos.requset.PostRequest;
import com.daviddai.blog.model.dtos.response.PostResponse;
import com.daviddai.blog.model.entities.Post;
import com.daviddai.blog.repositories.CategoryRepository;
import com.daviddai.blog.repositories.PostRepository;
import com.daviddai.blog.services.PostService;

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
        return postResponse;    }

    @Override
    public List<PostResponse> getAllPost() {
        log.info("PostService::getAllPost execution started");
        List<PostResponse> list = postRepository.findAll()
                .stream().map(PostMapper::mapToDto)
                .toList();
        log.info("PostService::getAllPost execution ended");
        return list;
    }

    @Override
    public List<PostResponse> getAllPostByTitle(String title) {
        log.info("PostService::getAllPostByTitle execution started");
        List<PostResponse> list = postRepository.findAllByTitleLike(title)
                        .stream().map(PostMapper::mapToDto)
                        .toList();
        log.info("PostService::getAllPostByTitle execution ended");
        return list;
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

}
