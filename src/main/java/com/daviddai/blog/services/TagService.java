package com.daviddai.blog.services;

import com.daviddai.blog.model.dtos.response.TagResponse;
import com.daviddai.blog.model.entities.Tag;

import java.util.List;

public interface TagService {
    public TagResponse getTagById(String id);

    public List<TagResponse> getAllTags();

    public TagResponse createTag(Tag tag);

    public TagResponse updateTag(String id, Tag tag);

    public void deleteTag(String id);
}
