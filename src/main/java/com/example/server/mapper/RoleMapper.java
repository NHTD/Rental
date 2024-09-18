package com.example.server.mapper;

import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.response.RoleResponse;
import com.example.server.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role roleToRole(RoleRequest request);
    RoleResponse roleToRoleResponse(Role role);
}
