package com.daviddai.blog.service.impl;

import com.daviddai.blog.dto.request.EmailUpdateRequest;
import com.daviddai.blog.dto.request.ProfileUpdateRequest;
import com.daviddai.blog.dto.request.UsernameUpdateRequest;
import com.daviddai.blog.dto.response.EmailChangeResponse;
import com.daviddai.blog.dto.response.UserResponse;
import com.daviddai.blog.dto.response.UsernameChangeResponse;
import com.daviddai.blog.entity.User;
import com.daviddai.blog.exception.UserAlreadyExistException;
import com.daviddai.blog.exception.UserNotFoundException;
import com.daviddai.blog.exception.UsernameNotAllowedException;
import com.daviddai.blog.mapper.UserMapper;
import com.daviddai.blog.repository.UserRepository;
import com.daviddai.blog.service.KeycloakAdminService;
import com.daviddai.blog.service.KeycloakService;
import com.daviddai.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    @Override
    public UserResponse getMyProfile(Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);

        User user = userRepository.findByUserId(userId)
                .orElseGet(() -> {
                    return createUserFromJWT(authentication);
                });
        return userMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateMyProfile(ProfileUpdateRequest request, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.isGender() != user.isGender()) {
            user.setGender(request.isGender());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        user = userRepository.save(user);
        return userMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public EmailChangeResponse updateMyEmail(EmailUpdateRequest request, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException("Email already exists");
        }

        try {
            keycloakService.updateUserEmail(userId, request.getEmail());
            keycloakService.triggerEmailVerification(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update email in identity provider", e);
        }

        user.setPendingEmail(request.getEmail());
        user.setEmailVerified(false);
        user.setIdentitySyncStatus("PENDING_EMAIL_VERIFY");
        userRepository.save(user);
        return EmailChangeResponse.builder()
                .verifyEmailTriggered(true)
                .pendingEmail(request.getEmail())
                .build();
    }

    @Override
    @Transactional
    public UsernameChangeResponse updateMyUsername(UsernameUpdateRequest request, Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);

        if (!keycloakService.isUsernameEditAllowed()) {
            throw new UsernameNotAllowedException("Username editing is not allowed");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistException("Username already exists");
        }

        keycloakService.updateUserUsername(userId, request.getUsername());

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(request.getUsername());
        userRepository.save(user);

        return UsernameChangeResponse.builder()
                .username(request.getUsername())
                .build();
    }

    @Override
    @Transactional
    public void deleteMyAccount(Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        keycloakService.disableUser(userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getAllUsers(String query, Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> userResponses = users.getContent().stream()
                .map(userMapper::mapToDto)
                .toList();

        return new PageImpl<>(userResponses, pageable, users.getTotalElements());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.mapToDto(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse updateUser(String id, ProfileUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.isGender() != user.isGender()) {
            user.setGender(request.isGender());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        user = userRepository.save(user);
        return userMapper.mapToDto(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void enableUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        keycloakService.enableUser(user.getUserId());
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void disableUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        keycloakService.disableUser(user.getUserId());
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        keycloakService.deleteUser(user.getUserId());
        userRepository.deleteById(id);
    }

    private User createUserFromJWT(Authentication authentication) {
        String userId = KeycloakAdminService.getUserId(authentication);
        String email = KeycloakAdminService.getEmail(authentication);
        String username = KeycloakAdminService.getUsername(authentication);
        String firstName = KeycloakAdminService.getFirstName(authentication);
        String lastName = KeycloakAdminService.getLastName(authentication);

        User user = User.builder()
                .userId(userId)
                .email(email)
                .username(username)
                .displayName(firstName + " " + lastName)
                .emailVerified(true)
                .enabled(true)
                .identitySyncStatus("ACTIVE")
                .build();

        return userRepository.save(user);
    }
}