package com.daviddai.blog.repositories;

import com.daviddai.blog.model.entities.Post;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post>  findBySlug(String slug);

    List<Post> findAllByTitleLike(String title);
}
