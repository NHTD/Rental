package com.example.server.services;

import com.example.server.dtos.request.PermissionRequest;
import com.example.server.dtos.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
}
