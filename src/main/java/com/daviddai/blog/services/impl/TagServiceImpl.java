package com.daviddai.blog.services.impl;

import java.util.List;

import com.daviddai.blog.exceptions.TagNotFoundException;
import com.daviddai.blog.mappers.TagMapper;
import com.daviddai.blog.model.dtos.response.TagResponse;
import com.daviddai.blog.model.entities.Tag;
import com.daviddai.blog.repositories.TagRepository;
import com.daviddai.blog.services.TagService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagResponse getTagById(String id) {
        log.info("TagService:getTagById execution started");
        Tag tag = tagRepository.findById(id)
                        .orElseThrow(() -> new TagNotFoundException("tag not found"));
        log.info("CategoryService:getTagById execution ended");
        return TagMapper.mapToDto(tag);
    }

    @Override
    public List<TagResponse> getAllTags() {
        log.info("TagService:getAllTags execution started");
        List<TagResponse> tags = tagRepository.findAll()
                .stream().map(TagMapper::mapToDto).toList();
        log.info("CategoryService:getAllTags execution ended");
        return tags;
    }

    @Override
    public TagResponse createTag(Tag tagRequest) {
        log.info("TagService:createTag execution started");
        Tag tag = tagRepository.save(tagRequest);
        log.info("CategoryService:createTag execution ended");
        return TagMapper.mapToDto(tag);
    }

    @Override
    public TagResponse updateTag(String id, Tag tagRequest) {
        log.info("TagService:updateTag execution started");
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("tag not found"));
        tag.setName(tagRequest.getName());
        tagRepository.save(tag);
        log.info("CategoryService:updateTag execution ended");
        return TagMapper.mapToDto(tag);
    }

    @Override
    public void deleteTag(String id) {
        log.info("TagService:deleteTag execution started");
        tagRepository.deleteById(id);
        log.info("CategoryService:deleteTag execution ended");
    }
}
