package com.example.server.services;

import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.response.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
}
