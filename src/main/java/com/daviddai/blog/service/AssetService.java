package com.daviddai.blog.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.daviddai.blog.dto.response.AssetDto;
import com.daviddai.blog.entity.Asset;
import com.daviddai.blog.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.daviddai.blog.exception.AssetNotFoundException;
import com.daviddai.blog.exception.AssetUploadException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final Cloudinary cloudinary;
    private final AssetRepository assetRepository;

    @Transactional
    public AssetDto upload(MultipartFile file, String userId) {
        validateFile(file);
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "blog-assets"
                )
            );

            Asset asset = new Asset();
            asset.setPublicId(uploadResult.get("public_id").toString());
            asset.setUrl(uploadResult.get("secure_url").toString());
            asset.setWidth(getIntValue(uploadResult, "width"));
            asset.setHeight(getIntValue(uploadResult, "height"));
            asset.setBytes(getLongValue(uploadResult, "bytes"));
            asset.setFormat(uploadResult.get("format").toString());
            asset.setCreatedBy(userId);
            asset.setCreatedAt(Instant.now());
            asset.setRefCount(0);

            Asset savedAsset = assetRepository.save(asset);
            
            return mapToDto(savedAsset);
        } catch (IOException e) {
            throw new AssetUploadException("Failed to upload asset to Cloudinary", e);
        } catch (Exception e) {
            throw new AssetUploadException("Unexpected error during asset upload: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void incrementRefCount(List<String> publicIds) {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }
        
        List<Asset> assets = assetRepository.findByPublicIdIn(publicIds);
        assets.forEach(asset -> {
            Integer currentRefCount = asset.getRefCount();
            asset.setRefCount(currentRefCount != null ? currentRefCount + 1 : 1);
            asset.setScheduledDeleteAt(null);
        });
        
        assetRepository.saveAll(assets);
    }

    @Transactional
    public void decrementRefCount(List<String> publicIds) {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }
        
        List<Asset> assets = assetRepository.findByPublicIdIn(publicIds);
        Instant deleteTime = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days
        
        assets.forEach(asset -> {
            Integer currentRefCount = asset.getRefCount();
            int newRefCount = Math.max(0, (currentRefCount != null ? currentRefCount : 0) - 1);
            asset.setRefCount(newRefCount);
            if (newRefCount == 0) {
                asset.setScheduledDeleteAt(deleteTime);
            }
        });
        
        assetRepository.saveAll(assets);
    }

    public Asset findByPublicId(String publicId) {
        return assetRepository.findByPublicId(publicId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with publicId: " + publicId));
    }

    public List<String> extractPublicIdsFromPostContents(List<com.daviddai.blog.entity.PostContent> contents) {
        List<String> publicIds = new ArrayList<>();
        if (contents == null || contents.isEmpty()) {
            return publicIds;
        }
        
        for (com.daviddai.blog.entity.PostContent postContent : contents) {
            if (postContent != null && postContent.getContent() != null) {
                for (org.bson.Document doc : postContent.getContent()) {
                    if (doc != null) {
                        extractPublicIdsRecursive(doc, publicIds);
                    }
                }
            }
        }
        return publicIds;
    }

    @SuppressWarnings("unchecked")
    private void extractPublicIdsRecursive(Object node, List<String> publicIds) {
        if (node instanceof Map) {
            Map<String, Object> nodeMap = (Map<String, Object>) node;
            
            if ("image".equals(nodeMap.get("type")) && nodeMap.containsKey("attrs")) {
                Object attrsObj = nodeMap.get("attrs");
                if (attrsObj instanceof Map) {
                    Map<String, Object> attrs = (Map<String, Object>) attrsObj;
                    Object publicIdObj = attrs.get("data-public-id");
                    if (publicIdObj instanceof String) {
                        String publicId = (String) publicIdObj;
                        if (!publicIds.contains(publicId)) {
                            publicIds.add(publicId);
                        }
                    }
                }
            }
            
            if (nodeMap.containsKey("content")) {
                Object content = nodeMap.get("content");
                if (content instanceof List) {
                    List<Object> contentList = (List<Object>) content;
                    for (Object child : contentList) {
                        extractPublicIdsRecursive(child, publicIds);
                    }
                }
            }
        } else if (node instanceof List) {
            List<Object> nodeList = (List<Object>) node;
            for (Object item : nodeList) {
                extractPublicIdsRecursive(item, publicIds);
            }
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AssetUploadException("File cannot be empty");
        }
        
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new AssetUploadException("Only image files are allowed");
        }
    }

    private Integer getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? ((Number) value).intValue() : null;
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? ((Number) value).longValue() : null;
    }

    private AssetDto mapToDto(Asset asset) {
        AssetDto dto = new AssetDto();
        dto.setPublicId(asset.getPublicId());
        dto.setUrl(asset.getUrl());
        dto.setWidth(asset.getWidth());
        dto.setHeight(asset.getHeight());
        dto.setFormat(asset.getFormat());
        dto.setBytes(asset.getBytes());
        return dto;
    }
}
