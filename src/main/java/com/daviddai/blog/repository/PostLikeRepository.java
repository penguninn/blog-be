package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.PostLike;
import com.daviddai.blog.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends MongoRepository<PostLike, String> {

    @Query("{'post.id': ?0, 'user.id': ?1}")
    Optional<PostLike> findByPostIdAndUserId(String postId, String userId);

    @Query("{'post.id': ?0}")
    long countByPostId(String postId);

    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);
}