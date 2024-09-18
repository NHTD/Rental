package com.example.server.controllers;

import com.example.server.dtos.request.PermissionRequest;
import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.response.PermissionResponse;
import com.example.server.dtos.response.RoleResponse;
import com.example.server.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.createPermission(request));
    }

}
