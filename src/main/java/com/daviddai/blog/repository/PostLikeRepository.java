package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.PostLike;
import com.daviddai.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {

    Optional<PostLike> findByPost_IdAndUser_Id(String postId, String userId);

    long countByPost_Id(String postId);

    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);
}
