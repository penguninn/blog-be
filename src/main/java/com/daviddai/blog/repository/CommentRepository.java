package com.daviddai.blog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daviddai.blog.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    
}
