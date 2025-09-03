package com.daviddai.blog.mapper;

import com.daviddai.blog.dto.response.UserResponse;
import com.daviddai.blog.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse mapToDto(User user);
}
