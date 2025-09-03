package com.daviddai.blog.service.impl;

import com.daviddai.blog.dto.request.CommentCreateRequest;
import com.daviddai.blog.dto.request.CommentUpdateRequest;
import com.daviddai.blog.dto.response.CommentResponse;
import com.daviddai.blog.dto.response.PageResponse;
import com.daviddai.blog.entity.Comment;
import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.User;
import com.daviddai.blog.exception.PostNotFoundException;
import com.daviddai.blog.mapper.CommentMapper;
import com.daviddai.blog.repository.CommentRepository;
import com.daviddai.blog.repository.PostRepository;
import com.daviddai.blog.repository.UserRepository;
import com.daviddai.blog.service.CommentService;
import com.daviddai.blog.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CommentCreateRequest request, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        log.info("Creating comment for post {} by user {}", request.getPostId(), userId);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + request.getPostId()));

        User author = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .author(author)
                .parentComment(parentComment)
                .isDeleted(false)
                .likesCount(0)
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created with ID: {}", savedComment.getId());

        return buildCommentResponse(savedComment);
    }

    @Override
    public PageResponse<CommentResponse> getCommentsByPost(String postId, int page, int size) {
        log.info("Getting comments for post: {}", postId);

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with ID: " + postId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Comment> commentsPage = commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable);

        List<CommentResponse> commentResponses = commentsPage.getContent().stream()
                .map(this::buildCommentResponse)
                .toList();

        return PageResponse.<CommentResponse>builder()
                .contents(commentResponses)
                .page(page)
                .size(size)
                .totalElements(commentsPage.getTotalElements())
                .totalPages(commentsPage.getTotalPages())
                .build();
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        log.info("Updating comment {} by user {}", commentId, userId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        if (comment.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Comment has been deleted");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return buildCommentResponse(updatedComment);
    }

    @Override
    public void deleteComment(String commentId, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        log.info("Deleting comment {} by user {}", commentId, userId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Override
    public CommentResponse getComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (comment.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Comment has been deleted");
        }

        return buildCommentResponse(comment);
    }

    private CommentResponse buildCommentResponse(Comment comment) {
        CommentResponse response = commentMapper.mapToDto(comment);
        
        if (comment.getParentComment() == null) {
            List<Comment> replies = commentRepository.findByParentCommentId(comment.getId());
            List<CommentResponse> replyResponses = replies.stream()
                    .map(commentMapper::mapToDto)
                    .toList();
            return CommentResponse.builder()
                    .id(response.getId())
                    .content(response.getContent())
                    .authorName(response.getAuthorName())
                    .authorId(response.getAuthorId())
                    .postId(response.getPostId())
                    .parentCommentId(response.getParentCommentId())
                    .likesCount(response.getLikesCount())
                    .replies(replyResponses)
                    .createdDate(response.getCreatedDate())
                    .modifiedDate(response.getModifiedDate())
                    .build();
        }
        
        return response;
    }
}