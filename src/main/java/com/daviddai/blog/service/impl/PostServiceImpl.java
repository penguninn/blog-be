package com.daviddai.blog.service.impl;

import com.daviddai.blog.dto.request.PostCreateRequest;
import com.daviddai.blog.dto.request.PostPublishRequest;
import com.daviddai.blog.dto.request.PostSearchRequest;
import com.daviddai.blog.dto.request.PostUpdateRequest;
import com.daviddai.blog.dto.response.PageResponse;
import com.daviddai.blog.dto.response.PostResponse;
import com.daviddai.blog.entity.Category;
import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.Tag;
import com.daviddai.blog.entity.User;
import com.daviddai.blog.enums.PostSortBy;
import com.daviddai.blog.enums.PostStatus;
import com.daviddai.blog.exception.CategoryNotFoundException;
import com.daviddai.blog.exception.PostNotFoundException;
import com.daviddai.blog.exception.TagNotFoundException;
import com.daviddai.blog.mapper.CategoryMapper;
import com.daviddai.blog.mapper.PostMapper;
import com.daviddai.blog.mapper.TagMapper;
import com.daviddai.blog.repository.CategoryRepository;
import com.daviddai.blog.repository.PostRepository;
import com.daviddai.blog.repository.TagRepository;
import com.daviddai.blog.repository.UserRepository;
import com.daviddai.blog.service.AssetService;
import com.daviddai.blog.service.PostService;
import com.daviddai.blog.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final AssetService assetService;

    @Override
    public PostResponse getPostById(String id, Authentication authentication) {
        boolean isAdmin = hasAdminRole(authentication);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        
        if (!isAdmin && post.getStatus() != PostStatus.PUBLISHED) {
            throw new PostNotFoundException("Post not found with id: " + id);
        }

        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(id)),
                new Update().inc("views", 1),
                Post.class);

        post.setViews(post.getViews() + 1);
        return mapPostToResponse(post);
    }

    @Override
    public PostResponse getPostBySlug(String slug, Authentication authentication) {
        boolean isAdmin = hasAdminRole(authentication);
        Post post = postRepository.findBySlugOrBySlugAndStatus(slug, PostStatus.PUBLISHED, isAdmin)
                .orElseThrow(() -> new PostNotFoundException("Post not found with slug: " + slug));

        mongoTemplate.updateFirst(
                Query.query(Criteria.where("slug").is(slug)),
                new Update().inc("views", 1),
                Post.class);

        post.setViews(post.getViews() + 1);
        return mapPostToResponse(post);
    }

    @Override
    public PageResponse<PostResponse> getAllPosts(int pageNo, int pageSize, PostSortBy sortBy,
            Sort.Direction direction, Authentication authentication) {
        boolean isAdmin = hasAdminRole(authentication);
        int page = Math.max(0, pageNo - 1);
        Sort sort = Sort.by(direction, sortBy.getFieldName());
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllByStatusOrAll(PostStatus.PUBLISHED, pageable, isAdmin);

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .page(posts.getNumber() + 1)
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PostResponse createPost(PostCreateRequest request, Authentication authentication) {
        User author = userRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new CategoryNotFoundException("Category not found");
        }

        if (!CollectionUtils.isEmpty(request.getTagIds())) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            if (tags.size() != request.getTagIds().size()) {
                throw new TagNotFoundException("One or more tags not found");
            }
        }

        String slug = generateUniqueSlug(request.getSlug(), request.getTitle());

        List<String> assetPublicIds = assetService.extractPublicIdsFromPostContents(request.getContents());
        assetService.incrementRefCount(assetPublicIds);

        Post post = Post.builder()
                .title(request.getTitle())
                .slug(slug)
                .status(request.getStatus())
                .userId(author.getUserId())
                .categoryId(request.getCategoryId())
                .tagIds(request.getTagIds())
                .contents(request.getContents())
                .assetPublicIds(assetPublicIds)
                .excerpt(request.getExcerpt())
                .build();

        return mapPostToResponse(postRepository.save(post));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PostResponse updatePost(String id, PostUpdateRequest request, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));

        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new CategoryNotFoundException("Category not found");
        }

        if (!CollectionUtils.isEmpty(request.getTagIds())) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            if (tags.size() != request.getTagIds().size()) {
                throw new TagNotFoundException("One or more tags not found");
            }
        }

        List<String> oldAssetPublicIds = post.getAssetPublicIds();
        List<String> newAssetPublicIds = assetService.extractPublicIdsFromPostContents(request.getContents());
        
        List<String> addedAssets = newAssetPublicIds.stream()
                .filter(publicId -> oldAssetPublicIds == null || !oldAssetPublicIds.contains(publicId))
                .toList();
        
        List<String> removedAssets = oldAssetPublicIds != null ? oldAssetPublicIds.stream()
                .filter(publicId -> !newAssetPublicIds.contains(publicId))
                .toList() : List.of();

        assetService.incrementRefCount(addedAssets);
        assetService.decrementRefCount(removedAssets);

        post.setTitle(request.getTitle());
        post.setStatus(request.getStatus());
        post.setCategoryId(request.getCategoryId());
        post.setTagIds(request.getTagIds());
        post.setContents(request.getContents());
        post.setAssetPublicIds(newAssetPublicIds);

        return mapPostToResponse(postRepository.save(post));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deletePost(String id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        
        if (post.getAssetPublicIds() != null) {
            assetService.decrementRefCount(post.getAssetPublicIds());
        }
        
        postRepository.deleteById(id);
    }

    @Override
    public PageResponse<PostResponse> getPostsByCategory(String categoryId, int pageNo, int pageSize,
            PostSortBy sortBy, Sort.Direction direction, Authentication authentication) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found");
        }

        boolean isAdmin = hasAdminRole(authentication);
        int page = Math.max(0, pageNo - 1);
        Sort sort = Sort.by(direction, sortBy.getFieldName());
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllByStatusAndCategoryIdOrAll(PostStatus.PUBLISHED, categoryId, pageable, isAdmin);

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .page(posts.getNumber() + 1)
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    public PageResponse<PostResponse> getPostsByTag(String tagId, int pageNo, int pageSize,
            PostSortBy sortBy, Sort.Direction direction, Authentication authentication) {
        if (!tagRepository.existsById(tagId)) {
            throw new TagNotFoundException("Tag not found");
        }

        boolean isAdmin = hasAdminRole(authentication);
        int page = Math.max(0, pageNo - 1);
        Sort sort = Sort.by(direction, sortBy.getFieldName());
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllByStatusAndTagIdsContainingOrAll(PostStatus.PUBLISHED, tagId, pageable, isAdmin);

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .page(posts.getNumber() + 1)
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Override
    public PageResponse<PostResponse> searchPosts(String query, int pageNo, int pageSize,
            PostSortBy sortBy, Sort.Direction direction, Authentication authentication) {
        boolean isAdmin = hasAdminRole(authentication);
        int page = Math.max(0, pageNo - 1);
        Sort sort = Sort.by(direction, sortBy.getFieldName());
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllBySlugContainingIgnoreCaseAndStatusOrAll(query, PostStatus.PUBLISHED, pageable, isAdmin);

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .page(posts.getNumber() + 1)
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    @Transactional
    public PostResponse publishPost(String postId, PostPublishRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));
        post.setStatus(PostStatus.PUBLISHED);
        post.setPublishedAt(java.time.Instant.now());
        Post savedPost = postRepository.save(post);
        return mapPostToResponse(savedPost);
    }

    @Transactional
    public PostResponse archivePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));

        post.setStatus(PostStatus.DRAFT);

        Post savedPost = postRepository.save(post);
        return mapPostToResponse(savedPost);
    }

    public PageResponse<PostResponse> getDraftPosts(int pageNo, int pageSize, PostSortBy sortBy,
            Sort.Direction direction, Authentication authentication) {
        int page = Math.max(0, pageNo - 1);
        Sort sort = Sort.by(direction, sortBy.getFieldName());
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllByStatus(PostStatus.DRAFT, pageable);

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapPostToResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .page(posts.getNumber() + 1)
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .contents(postResponses)
                .build();
    }

    private String generateUniqueSlug(String providedSlug, String title) {
        String baseSlug;
        if (StringUtils.hasText(providedSlug)) {
            baseSlug = SlugUtils.generateSlug(providedSlug);
        } else {
            baseSlug = SlugUtils.generateSlug(title);
        }
        
        while (postRepository.existsBySlug(baseSlug)) {
            baseSlug = SlugUtils.generateSlug(title);
        }
        
        return baseSlug;
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private PostResponse mapPostToResponse(Post post) {
        User user = null;
        Category category = null;
        List<Tag> tags = null;

        if (StringUtils.hasText(post.getUserId())) {
            user = userRepository.findByUserId(post.getUserId()).orElse(null);
        }

        if (StringUtils.hasText(post.getCategoryId())) {
            category = categoryRepository.findById(post.getCategoryId()).orElse(null);
        }

        if (!CollectionUtils.isEmpty(post.getTagIds())) {
            tags = tagRepository.findAllById(post.getTagIds());
        }

        return postMapper.mapToDto(post, user, category, tags);
    }
}