package com.daviddai.blog.repository;

import com.daviddai.blog.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    
    Optional<Asset> findByPublicId(String publicId);
    
    List<Asset> findByPublicIdIn(List<String> publicIds);
    
    List<Asset> findByScheduledDeleteAtIsNotNullAndScheduledDeleteAtBefore(Instant now);
}
