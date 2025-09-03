package com.daviddai.blog.service;

import com.daviddai.blog.dto.request.TagRequest;
import com.daviddai.blog.dto.response.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse getTagById(String id);

    List<TagResponse> getAllTags();

    TagResponse createTag(TagRequest tagRequest);

    TagResponse updateTag(String id, TagRequest tagRequest);

    void deleteTag(String id);
}
