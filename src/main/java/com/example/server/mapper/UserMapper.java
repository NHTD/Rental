package com.example.server.mapper;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.request.UserUpdateRequest;
import com.example.server.dtos.response.UserResponse;
import com.example.server.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User userToUser(UserRequest request);
    UserResponse userToUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void userToUserUpdate(@MappingTarget User user, UserUpdateRequest request);
}
