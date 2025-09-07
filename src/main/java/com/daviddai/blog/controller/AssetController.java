package com.daviddai.blog.controller;

import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.AssetDto;
import com.daviddai.blog.service.AssetService;
import com.daviddai.blog.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Assets", description = "Asset management endpoints")
public class AssetController {

    private final AssetService assetService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadAsset(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        AssetDto assetDto = assetService.upload(file, authentication.getName());
        return ApiResponseBuilder.created(assetDto, "Asset uploaded successfully");
    }
}
