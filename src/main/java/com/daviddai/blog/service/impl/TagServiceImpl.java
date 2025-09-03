package com.daviddai.blog.service.impl;

import com.daviddai.blog.dto.request.TagRequest;
import com.daviddai.blog.dto.response.TagResponse;
import com.daviddai.blog.entity.Tag;
import com.daviddai.blog.exception.TagNotFoundException;
import com.daviddai.blog.mapper.TagMapper;
import com.daviddai.blog.repository.PostRepository;
import com.daviddai.blog.repository.TagRepository;
import com.daviddai.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponse getTagById(String id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));
        return tagMapper.mapToDto(tag);
    }

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::mapToDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TagResponse createTag(TagRequest tagRequest) {
        Tag tag = Tag.builder()
                .name(tagRequest.getName())
                .build();
        return tagMapper.mapToDto(tagRepository.save(tag));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TagResponse updateTag(String id, TagRequest tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));
        tag.setName(tagRequest.getName());
        return tagMapper.mapToDto(tagRepository.save(tag));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteTag(String id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        if (postRepository.existsByTagIdsContaining(id)) {
            throw new IllegalStateException("Cannot delete tag that is referenced by posts");
        }

        tagRepository.deleteById(id);
    }
}
