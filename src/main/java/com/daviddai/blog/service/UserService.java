package com.daviddai.blog.service;

import com.daviddai.blog.dto.request.EmailUpdateRequest;
import com.daviddai.blog.dto.request.ProfileUpdateRequest;
import com.daviddai.blog.dto.request.UsernameUpdateRequest;
import com.daviddai.blog.dto.response.EmailChangeResponse;
import com.daviddai.blog.dto.response.UserResponse;
import com.daviddai.blog.dto.response.UsernameChangeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponse getMyProfile(Authentication authentication);

    UserResponse updateMyProfile(ProfileUpdateRequest request, Authentication authentication);

    EmailChangeResponse updateMyEmail(EmailUpdateRequest request, Authentication authentication);

    UsernameChangeResponse updateMyUsername(UsernameUpdateRequest request, Authentication authentication);

    void deleteMyAccount(Authentication authentication);

    Page<UserResponse> getAllUsers(String query, Pageable pageable);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, ProfileUpdateRequest request);

    void enableUser(String id);

    void disableUser(String id);

    void deleteUser(String id);

}
