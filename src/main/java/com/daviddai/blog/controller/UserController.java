package com.daviddai.blog.controller;

import com.daviddai.blog.dto.request.EmailUpdateRequest;
import com.daviddai.blog.dto.request.ProfileUpdateRequest;
import com.daviddai.blog.dto.request.UsernameUpdateRequest;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.EmailChangeResponse;
import com.daviddai.blog.dto.response.UserResponse;
import com.daviddai.blog.dto.response.UsernameChangeResponse;
import com.daviddai.blog.service.UserService;
import com.daviddai.blog.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(Authentication authentication) {
        UserResponse profile = userService.getMyProfile(authentication);
        return ApiResponseBuilder.success(profile, "Profile retrieved successfully");
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(
            @Valid @RequestBody ProfileUpdateRequest request,
            Authentication authentication) {
        UserResponse profile = userService.updateMyProfile(request, authentication);
        return ApiResponseBuilder.success(profile, "Profile updated successfully");
    }

    @PatchMapping("/me/email")
    public ResponseEntity<ApiResponse<EmailChangeResponse>> updateMyEmail(
            @Valid @RequestBody EmailUpdateRequest request,
            Authentication authentication) {
        EmailChangeResponse response = userService.updateMyEmail(request, authentication);
        return ApiResponseBuilder.success(response, "Email update requested successfully");
    }

    @PatchMapping("/me/username")
    public ResponseEntity<ApiResponse<UsernameChangeResponse>> updateMyUsername(
            @Valid @RequestBody UsernameUpdateRequest request,
            Authentication authentication) {
        UsernameChangeResponse response = userService.updateMyUsername(request, authentication);
        return ApiResponseBuilder.success(response, "Username updated successfully");
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(Authentication authentication) {
        userService.deleteMyAccount(authentication);
        return ApiResponseBuilder.noContent("Account deleted successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String q,
            Pageable pageable) {
        Page<UserResponse> userPage = userService.getAllUsers(q, pageable);
        return ApiResponseBuilder.successWithPagination(userPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return ApiResponseBuilder.success(user, "User retrieved successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody ProfileUpdateRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ApiResponseBuilder.success(user, "User updated successfully");
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable String id) {
        userService.enableUser(id);
        return ApiResponseBuilder.success(null, "User enabled successfully");
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable String id) {
        userService.disableUser(id);
        return ApiResponseBuilder.success(null, "User disabled successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponseBuilder.noContent("User deleted successfully");
    }
}
