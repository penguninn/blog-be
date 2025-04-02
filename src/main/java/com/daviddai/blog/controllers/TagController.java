package com.daviddai.blog.controllers;

import com.daviddai.blog.model.dtos.response.TagResponse;
import com.daviddai.blog.model.dtos.response.ResponseSuccess;
import com.daviddai.blog.model.entities.Tag;
import com.daviddai.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
@Slf4j
public class TagController {
    
    private final TagService tagService;

    @GetMapping("/{id}")
    public ResponseSuccess<?> getTag(@Valid @PathVariable String id) {
        log.info("TagController::getTag execution started");
        TagResponse tag = tagService.getTagById(id);
        log.info("TagController::getTag execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get tag successfully", tag);
    }

    @PostMapping
    public ResponseSuccess<?> createTag(@Valid @RequestBody Tag tag) {
        log.info("TagController::createTag execution started");
        TagResponse result = tagService.createTag(tag);
        log.info("TagController::createTag execution ended");
        return new ResponseSuccess<>(HttpStatus.CREATED, "Create new tag successfully", result);
    }

    @GetMapping
    public ResponseSuccess<?> getAllTags() {
        log.info("TagController::getAllTags execution started");
        List<TagResponse> tags = tagService.getAllTags();
        log.info("TagController::getAllTags execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Get all tag successfully", tags);
    }

    @PutMapping("/{id}")
    public ResponseSuccess<?> updateTag(@Valid @PathVariable String id, @Valid @RequestBody Tag tagRequest) {
        log.info("TagController::updateTag execution started");
        TagResponse tag = tagService.updateTag(id, tagRequest);
        log.info("TagController::updateTag execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Update tag successfully", tag);
    }

    @DeleteMapping("/{id}")
    public ResponseSuccess<?> deleteTag(@Valid @PathVariable String id) {
        log.info("TagController::deleteTag execution started");
        tagService.deleteTag(id);
        log.info("TagController::deleteTag execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Delete Tag successfully");
    }
}
