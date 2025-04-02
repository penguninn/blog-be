package com.daviddai.blog.repositories;

import com.daviddai.blog.model.entities.Token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

}
