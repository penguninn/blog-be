package com.daviddai.blog.repositories;

import com.daviddai.blog.model.entities.Token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    
    Optional<Token> findByRefreshToken(String refreshToken);
    
    @Query("{'revoked': false, 'expried': false}")
    List<Token> findAllValidTokens();
}
