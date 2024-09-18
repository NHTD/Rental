package com.example.server.mapper;

import com.example.server.dtos.request.PermissionRequest;
import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.response.PermissionResponse;
import com.example.server.dtos.response.RoleResponse;
import com.example.server.models.Permission;
import com.example.server.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission permissionToPermission(PermissionRequest request);
    PermissionResponse permissionToPermissionResponse(Permission permission);
}
