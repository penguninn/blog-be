package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Post;
import com.daviddai.blog.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findBySlug(String slug);
    
    Optional<Post> findBySlugAndStatus(String slug, PostStatus status);
    
    Page<Post> findAllByStatus(PostStatus status, Pageable pageable);
    
    Page<Post> findAllByStatusAndCategoryId(PostStatus status, String categoryId, Pageable pageable);
    
    
    Page<Post> findAllByStatusAndUserId(PostStatus status, String userId, Pageable pageable);
    
    Page<Post> findAllBySlugContainingIgnoreCaseAndStatus(String slug, PostStatus status, Pageable pageable);
    
    Page<Post> findAllBySlugContainingIgnoreCase(String slug, Pageable pageable);
    
    Page<Post> findAllByCategoryId(String categoryId, Pageable pageable);
    
    
    boolean existsByCategoryId(String categoryId);
    
    
    boolean existsBySlug(String slug);

    default Optional<Post> findBySlugOrBySlugAndStatus(String slug, PostStatus status, boolean isAdmin) {
        if (isAdmin) {
            return findBySlug(slug);
        } else {
            return findBySlugAndStatus(slug, status);
        }
    }

    default Page<Post> findAllByStatusOrAll(PostStatus status, Pageable pageable, boolean isAdmin) {
        if (isAdmin) {
            return findAll(pageable);
        } else {
            return findAllByStatus(status, pageable);
        }
    }

    default Page<Post> findAllByStatusAndCategoryIdOrAll(PostStatus status, String categoryId, Pageable pageable, boolean isAdmin) {
        if (isAdmin) {
            return findAllByCategoryId(categoryId, pageable);
        } else {
            return findAllByStatusAndCategoryId(status, categoryId, pageable);
        }
    }


    default Page<Post> findAllBySlugContainingIgnoreCaseAndStatusOrAll(String slug, PostStatus status, Pageable pageable, boolean isAdmin) {
        if (isAdmin) {
            return findAllBySlugContainingIgnoreCase(slug, pageable);
        } else {
            return findAllBySlugContainingIgnoreCaseAndStatus(slug, status, pageable);
        }
    }

}
