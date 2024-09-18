package com.example.server.services.servicesImp;

import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.response.RoleResponse;
import com.example.server.mapper.RoleMapper;
import com.example.server.models.Permission;
import com.example.server.models.Role;
import com.example.server.repositories.PermissionRepository;
import com.example.server.repositories.RoleRepository;
import com.example.server.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.roleToRole(request);

        List<Permission> permissions = permissionRepository.findAll();
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.roleToRoleResponse(roleRepository.save(role));
    }

}
