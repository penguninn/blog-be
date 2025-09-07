package com.daviddai.blog.job;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.daviddai.blog.entity.Asset;
import com.daviddai.blog.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanupJob {

    private final AssetRepository assetRepository;
    private final Cloudinary cloudinary;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupExpiredAssets() {
        log.info("Starting cleanup of expired assets");
        
        try {
            List<Asset> expiredAssets = assetRepository.findExpiredAssets(Instant.now());
            
            if (expiredAssets.isEmpty()) {
                log.info("No expired assets found");
                return;
            }
            
            log.info("Found {} expired assets to cleanup", expiredAssets.size());
            
            int deletedFromCloudinary = 0;
            int deletedFromDatabase = 0;
            
            for (Asset asset : expiredAssets) {
                try {
                    cloudinary.uploader().destroy(
                        asset.getPublicId(), 
                        ObjectUtils.asMap("resource_type", "image")
                    );
                    deletedFromCloudinary++;
                    log.debug("Deleted asset from Cloudinary: {}", asset.getPublicId());
                } catch (Exception e) {
                    log.warn("Failed to delete asset from Cloudinary: {}, error: {}", 
                        asset.getPublicId(), e.getMessage());
                }
                
                try {
                    assetRepository.delete(asset);
                    deletedFromDatabase++;
                    log.debug("Deleted asset from database: {}", asset.getPublicId());
                } catch (Exception e) {
                    log.error("Failed to delete asset from database: {}, error: {}", 
                        asset.getPublicId(), e.getMessage());
                }
            }
            
            log.info("Cleanup completed. Deleted {} from Cloudinary, {} from database", 
                deletedFromCloudinary, deletedFromDatabase);
                
        } catch (Exception e) {
            log.error("Error during asset cleanup", e);
        }
    }
}