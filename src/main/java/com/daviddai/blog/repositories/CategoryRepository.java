package com.daviddai.blog.repositories;

import com.daviddai.blog.model.entities.Category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    
}
