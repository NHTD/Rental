package com.example.server.controllers;

import com.example.server.dtos.request.RoleRequest;
import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.RoleResponse;
import com.example.server.dtos.response.UserResponse;
import com.example.server.services.RoleService;
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
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ResponseEntity<RoleResponse> createUser(@RequestBody RoleRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(roleService.createRole(request));
    }

}
