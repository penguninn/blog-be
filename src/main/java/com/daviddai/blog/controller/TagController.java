package com.daviddai.blog.controller;

import com.daviddai.blog.dto.request.TagRequest;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.TagResponse;
import com.daviddai.blog.service.TagService;
import com.daviddai.blog.utils.ApiResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ApiResponseBuilder.success(tags, "Tags retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> getTag(@PathVariable String id) {
        TagResponse tag = tagService.getTagById(id);
        return ApiResponseBuilder.success(tag, "Tag retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagRequest tagRequest) {
        TagResponse tag = tagService.createTag(tagRequest);
        return ApiResponseBuilder.created(tag, "Tag created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(@PathVariable String id,
            @Valid @RequestBody TagRequest tagRequest) {
        TagResponse tag = tagService.updateTag(id, tagRequest);
        return ApiResponseBuilder.success(tag, "Tag updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
        return ApiResponseBuilder.noContent("Tag deleted successfully");
    }
}
