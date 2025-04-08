package com.daviddai.blog.repositories;

import java.util.Optional;

import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.model.entities.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findBySlug(String slug);

    Page<Post> findAllBySlugLike(String slug, Pageable pageable);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    @Query("{ 'tags.id': ?0 }")
    Page<Post> findAllByTagId(String tagId, Pageable pageable);
}
