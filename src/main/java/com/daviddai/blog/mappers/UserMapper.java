package com.daviddai.blog.mappers;

import java.util.Collection;
import java.util.stream.Collectors;

import com.daviddai.blog.model.dtos.request.UserRegisterRequest;
import com.daviddai.blog.model.dtos.response.ProfileResponse;
import com.daviddai.blog.model.entities.User;
import com.daviddai.blog.security.UserDetailsImpl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserMapper {

    public static UserDetailsImpl mapToUserDetails(User user) {
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .collect(Collectors.toList());
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

    }

    public static User mapToEntity(UserRegisterRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    public static ProfileResponse mapToProfile(User user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
