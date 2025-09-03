package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Comment;
import com.daviddai.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{'post.id': ?0, 'isDeleted': false, 'parentComment': null}")
    Page<Comment> findByPostIdAndParentCommentIsNull(String postId, Pageable pageable);

    @Query("{'parentComment.id': ?0, 'isDeleted': false}")
    List<Comment> findByParentCommentId(String parentCommentId);

    @Query("{'author.id': ?0, 'isDeleted': false}")
    Page<Comment> findByAuthorId(String authorId, Pageable pageable);

    @Query("{'post.id': ?0, 'isDeleted': false}")
    long countByPostId(String postId);
}