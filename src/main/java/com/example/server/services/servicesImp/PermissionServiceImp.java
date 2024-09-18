package com.example.server.services.servicesImp;

import com.example.server.dtos.request.PermissionRequest;
import com.example.server.dtos.response.PermissionResponse;
import com.example.server.mapper.PermissionMapper;
import com.example.server.models.Permission;
import com.example.server.repositories.PermissionRepository;
import com.example.server.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.permissionToPermission(request);

        return permissionMapper.permissionToPermissionResponse(permissionRepository.save(permission));
    }
}
