package com.daviddai.blog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daviddai.blog.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    
}
