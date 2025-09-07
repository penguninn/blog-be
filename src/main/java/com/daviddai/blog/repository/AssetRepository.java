package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {
    
    Optional<Asset> findByPublicId(String publicId);
    
    List<Asset> findByPublicIdIn(List<String> publicIds);
    
    @Query("{ 'scheduledDeleteAt' : { $ne: null, $lt: ?0 } }")
    List<Asset> findExpiredAssets(Instant now);
}