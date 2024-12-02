package com.example.recomme_be.mapper;

import com.example.recomme_be.dto.request.UserCreationRequest;
import com.example.recomme_be.dto.request.UserUpdateRequest;
import com.example.recomme_be.dto.response.UserResponse;
import com.example.recomme_be.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
