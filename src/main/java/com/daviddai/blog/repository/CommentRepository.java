package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Page<Comment> findByPost_IdAndIsDeletedFalseAndParentCommentIsNull(String postId, Pageable pageable);

    List<Comment> findByParentComment_IdAndIsDeletedFalse(String parentCommentId);

    Page<Comment> findByAuthor_IdAndIsDeletedFalse(String authorId, Pageable pageable);

    long countByPost_IdAndIsDeletedFalse(String postId);
}
