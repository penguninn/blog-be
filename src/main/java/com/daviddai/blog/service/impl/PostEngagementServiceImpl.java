package com.daviddai.blog.service.impl;

import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.PostLike;
import com.daviddai.blog.entity.User;
import com.daviddai.blog.exception.PostNotFoundException;
import com.daviddai.blog.repository.PostLikeRepository;
import com.daviddai.blog.repository.PostRepository;
import com.daviddai.blog.repository.UserRepository;
import com.daviddai.blog.service.KeycloakAdminService;
import com.daviddai.blog.service.PostEngagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostEngagementServiceImpl implements PostEngagementService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void incrementViews(String postId) {
        log.info("Incrementing views for post: {}", postId);
        
        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update().inc("views", 1);
        
        mongoTemplate.updateFirst(query, update, Post.class);
    }

    @Override
    @Transactional
    public boolean toggleLike(String postId, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        log.info("Toggling like for post {} by user {}", postId, userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserId(postId, user.getId());

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            updatePostLikesCount(postId, -1);
            log.info("Removed like for post {} by user {}", postId, userId);
            return false;
        } else {
            PostLike newLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            postLikeRepository.save(newLike);
            updatePostLikesCount(postId, 1);
            log.info("Added like for post {} by user {}", postId, userId);
            return true;
        }
    }

    @Override
    public long getLikesCount(String postId) {
        return postLikeRepository.countByPostId(postId);
    }

    @Override
    public boolean isLikedByUser(String postId, Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        
        String userId = KeycloakAdminService.getUserId(authentication);
        User user = userRepository.findByUserId(userId).orElse(null);
        
        if (user == null) {
            return false;
        }
        
        return postLikeRepository.findByPostIdAndUserId(postId, user.getId()).isPresent();
    }

    private void updatePostLikesCount(String postId, int delta) {
        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update().inc("likesCount", delta);
        mongoTemplate.updateFirst(query, update, Post.class);
    }
}